package com.progressive.minds.chimera.DataManagement.datalineage

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.openlineage.client.OpenLineage.ColumnLineageDatasetFacetFieldsAdditional
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.expressions.Alias
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.plans.logical._
import play.api.libs.json._
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.asJavaIterableConverter
import scala.util.matching.Regex

object ColumnLineageUtils {
  var loggerTag = "Column Lineage Utils"
// val outputTableList = ""
// val rawSQLLineage: Seq[ColumnLineageRecord] = extractColumnLineage(
  // analyzedLogicalPlan, targetTable = outputTableList)

  case class Normalized(TableName: String, ColumnName: String, ColumnId: String, parentColumns: List[String])

  private case class NormalizedHire(LookupTable: String, LookUpColumn: String, LookupKey: String,
      LookupTransform: String) extends Serializable

  private case class NormalizedHireDetails(parentDetails: NormalizedHire, childInfo: List[NormalizedHire])

  private case class finalOutput(inputTable: List[String], outputTable: List[String],
      descendant: List[NormalizedHireDetails])

  private case class Columns(name: String, parentColumns: List[String])

  private case class MappingList(child: String, parent: String)

  private case class TransformMapping(inFinalOutput: List[NormalizedHire],
      appendList: ListBuffer[(String, String, String)], inMap: List[MappingList])

  case class ColumnLineageRecord(targetTableName: String,derivedColumnId: String,derivedColumnName: String,
      sourceTable: String, inputColumns: Seq[String], transformation: String,parentColumnId: String)

  private def getTableColumnMapping(analyzedPlan: LogicalPlan, pattern: String = "View"): List[NormalizedHire] = {
    var parentRecords: List[NormalizedHire] = List()

    val patFilter = analyzedPlan.toString().split("\n")
      .collect { case s: Any if s.contains(s"$pattern (") => s.substring(s.indexOf(s"$pattern ("))}
    patFilter.foreach { pat =>
      val parts =
        pat.stripPrefix(s"$pattern (").stripSuffix(")").split(",", 2)
      val tabName = parts(0).trim.stripPrefix("`").stripSuffix("`")
      val list = parts(1).trim.stripPrefix("[").stripSuffix("]").split("[(,);]")
      val splits = list.map(_.split("#"))
      splits.foreach { case Array(part1, part2) =>
        val parentInfo = NormalizedHire(LookupTable = tabName,
          LookUpColumn = part1, LookupKey = part2.filter(_.isDigit),
          LookupTransform = "STP")
        parentRecords = parentRecords :+ parentInfo
      }
    }
    parentRecords = parentRecords.distinct
    parentRecords
  }

  private def resolvedNumberValue(inExpression: String): String = {
    // import scala.util.matching.Regex
    val numberPattern: Regex = "#(\\d+)".r
    numberPattern.findAllIn(inExpression).mkString(",").replaceAll("#", "")
  }

  private def removeElement(list: List[ColumnLineageRecord], element: String): List[ColumnLineageRecord] = {
    val firstList = list.filterNot(p => p.derivedColumnId == element && p.parentColumnId == p.derivedColumnId)
    val processedList = firstList.map { fl =>
      if (fl.parentColumnId.contains(element)) {
        fl.copy(parentColumnId = fl.parentColumnId.replace(s"$element,", "").replace(s",$element", ""))
      }
      else {
        fl
      }
    }
    processedList
  }

  private def removeAllOccurrences[A](list: List[A], element: A): List[A] = {
    list.filterNot(_ == element)
  }

  def resolvedValue(inExpression: String): Seq[String] = {
    inExpression.replaceAll("", ";").split("[();]").filter(f => f.contains("#")).distinct
  }

  private def removeHash(input: String): String = {
    val pattern = "#\\d+$".r
    val output = pattern.replaceAllIn(input, "")
    output
  }

  private def createLineage(finalList: finalOutput): String = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    // Create the root node
    val rootNode: ObjectNode = mapper.createObjectNode()
    // Create inputTables array
    rootNode.set("inTables", mapper.valueToTree(finalList.inputTable))
    // Create OutTables array
    rootNode.set("outTables", mapper.valueToTree(finalList.outputTable))
    // Create columnLineage array
    val columnLineageArray: ArrayNode = mapper.createArrayNode()

    finalList.descendant.foreach { des =>

      val maps: List[Map[String, String]] = des.childInfo.map { child =>
        Map(
          "origin" -> child.LookupTable,
          "name" -> removeHash(child.LookUpColumn),
          "transformation" -> Option(child.LookupTransform).getOrElse("STP"))
      }

      val anc: ObjectNode = mapper.createObjectNode()
      val LinNode: ObjectNode = mapper.createObjectNode()
      anc.put("origin", des.parentDetails.LookupTable)
      anc.put("name", removeHash(des.parentDetails.LookUpColumn))
      LinNode.set("descendant", anc)
      LinNode.set("lineage", mapper.valueToTree(maps))
      columnLineageArray.add(LinNode)
    }
    // Add columnLineage array to the root node
    rootNode.set("columnLineage", columnLineageArray)
    // Convert to J30N string
    val jsonString: String = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode)
    jsonString
  }

  // =
  private def extractColumnLineage(plan: LogicalPlan, aliasMap: Map[String, String] = Map(), targetTable: String = ""):
    Seq[ColumnLineageRecord] = {
    plan match {
      case Project(projectList, child) =>
        val projectLineage = projectList.map { expr =>
          val derivedColumn = expr match {
            case Alias(_, name) => name
            case _ => expr.name.concat("#").concat(expr.exprId.id.toString)
          }
          val inputColumns = expr.references.map(_.name).toSeq
          val sourceTables = expr.references.collect {
            case attr: AttributeReference =>
              aliasMap.getOrElse(attr.qualifier.headOption.getOrElse(""), attr.qualifier.headOption.getOrElse(""))
          }.toSeq.distinct
          ColumnLineageRecord(targetTable, expr.exprId.id.toString, derivedColumn, sourceTables.mkString(","),
            inputColumns,
            s"""${expr.toString()}""",
            resolvedNumberValue(expr.toString()))
        }
        projectLineage ++ extractColumnLineage(child, aliasMap, targetTable)

      case Join(left, right, _, _, _) =>
        extractColumnLineage(left, aliasMap, targetTable) ++ extractColumnLineage(right, aliasMap, targetTable)

      case Aggregate(_, aggregateExpressions, child) =>
        val aggregateLineage = aggregateExpressions.map { expr =>

          val derivedColumn = expr match {
            case Alias(_, name) => name
            case _ => expr.prettyName
          }
          val inputColumns = expr.references.map(_.name).toSeq
          val sourceTables = expr.references.collect {
            case attr: AttributeReference =>
              aliasMap.getOrElse(attr.qualifier.headOption.getOrElse(""), attr.qualifier.headOption.getOrElse(", "))
          }.toSeq.distinct

          ColumnLineageRecord(targetTable, expr.exprId.id.toString, derivedColumn,
            sourceTables.mkString(",")
            , inputColumns
            , s"${expr.toString()}"
            ,
            resolvedNumberValue(expr.toString)
          )
        }
        aggregateLineage ++ extractColumnLineage(child, aliasMap, targetTable)

      case Filter(_, child) =>
        extractColumnLineage(child, aliasMap, targetTable)

      case View(_, _, child) =>
        extractColumnLineage(child, aliasMap, targetTable)

      case SubqueryAlias(alias, child) =>
        val updatedAliasMap = child.output.map(attr => attr.name -> alias.name).toMap
        extractColumnLineage(child, aliasMap ++ updatedAliasMap, alias.name)

      case other : Any => other.children.flatMap(child => extractColumnLineage(child, aliasMap, targetTable))
    }
  }

  def findDifferentials(seq1: Seq[ColumnLineageRecord], seq2: Seq[Normalized]): Seq[ColumnLineageRecord] = {
    val set1 = seq1.map(_.parentColumnId).toSet
    val set2 = seq2.map(_.ColumnId).toSet
    seq1.filterNot(record => set2.contains(record.parentColumnId))
  }

  private def buildHierarchy(inColumns: List[Columns]): Map[String, List[String]] = {
    // Create a map from column name to parent columns
    val parentMap = inColumns.map(col => col.name -> col.parentColumns).toMap
    // Reverse the parent map to get child columns for each parent

    val childMap = inColumns.flatMap { col =>
      col.parentColumns.map(parent => parent -> col.name)
    }.groupBy(_._1).mapValues(_.map(_._2))
    childMap
  }

  private def printHierarchy(columnName: String, hierarchy: Map[String, List[String]], level: Int = 0): Unit = {
    val indent = "   " * level
    println(s"$indent$columnName")
    hierarchy.getOrElse(columnName, List()).foreach { childColumn =>
      printHierarchy(childColumn, hierarchy, level + 1)
    }
  }

  private def printAllHierarchies(hierarchy: Map[String, List[String]]): Unit = {
    // Find root columns (those not listed as children)
    val allChildren = hierarchy.values.flatten.toSet
    val rootColumns = hierarchy.keySet.diff(allChildren)
    rootColumns.foreach { column => printHierarchy(column, hierarchy) }
  }

  private def manageMapping(child: String, parent: String): List[MappingList] = {
    var columnTemp: List[MappingList] = List()
    val isChildContainsComma = child.contains(",")
    val isParentContainsComma = parent.contains(",")

    (isParentContainsComma, isChildContainsComma) match {
      case (false, true) =>
        val valuesList = child.split(",").distinct.toList
        val formattedList = valuesList.map(v => MappingList(parent, v))
        columnTemp = columnTemp ++ formattedList
        columnTemp
      case (true, false) =>
        val valuesList = parent.split(",").distinct.toList
        val formattedList = valuesList.map(v => MappingList(v, child))
        columnTemp = columnTemp ++ formattedList
        columnTemp

      case _ =>
        val formattedList = parent.map(v => MappingList(parent, child))
        columnTemp = columnTemp ++ formattedList
        columnTemp
    }
  }

  private def columnJson(dataframe: DataFrame, spark: SparkSession): String = {
    val analyzedLogicalPlan = dataframe.queryExecution.analyzed
    val unresolvedLogicalPlan = dataframe.queryExecution.logical
    val inputTableList = unresolvedLogicalPlan.collect { case r: UnresolvedRelation => r.tableName }.toSet
    val outputTableList = ""
    val rawSQLLineage: Seq[ColumnLineageRecord] =
      extractColumnLineage(analyzedLogicalPlan, targetTable = outputTableList)

    var parentRecords: List[NormalizedHire] = List()
    var parentList: List[String] = List()
    var isParent: String = "N"
    var returnedRecords: List[NormalizedHire] = List()
    var finalRecords: List[NormalizedHireDetails] = List()
    var transformationMappingList = ListBuffer.empty[(String, String, String)]
    val tabLevelMapping = getTableColumnMapping(analyzedLogicalPlan)
    parentRecords = (parentRecords ++ tabLevelMapping).distinct
    var columnTemp: List[MappingList] = List()

    val baseTableColumns = rawSQLLineage.filter(base => base.targetTableName.equalsIgnoreCase(outputTableList))

    baseTableColumns.foreach(column => {
      val derievedColumnId = column.derivedColumnId
      val parentRecAdd = NormalizedHire(column.targetTableName, column.derivedColumnName,
        column.derivedColumnId, column.transformation)

      val isRoot = parentRecords.filter(p => p.LookupKey.equalsIgnoreCase(derievedColumnId))
      // edlLogger.logInfo("Search For Output", s"Searching For ${column.targetTableName} & "
      // + s"Column ${column.derivedColumnName}")

      isRoot.size match {
        case 0 =>
          val baseToSearch = rawSQLLineage.filter(base => base.derivedColumnId.contains(derievedColumnId) &&
            !base.parentColumnId.equalsIgnoreCase(derievedColumnId))

          val filtered_Rec = removeElement(baseToSearch.toList, derievedColumnId)
          parentList = filtered_Rec.map(_.parentColumnId).mkString.split(",").distinct.toList
          val retVal = callFromParent(column.parentColumnId, parentList, column, rawSQLLineage, parentRecords)

          returnedRecords = retVal.inFinalOutput

          transformationMappingList = transformationMappingList ++ retVal.appendList
          columnTemp = columnTemp ++ retVal.inMap
          // edlLogger.logInfo("Completed ", s"Searching For ${column.targetTableName} & "
          // + s"Column ${column.derivedColumnName}\n")
          // edlLogger.logInfo("", "FINISHED\n")

        case _ =>
          parentList = List(derievedColumnId)
          isParent = "Y"
          returnedRecords = List(NormalizedHire(column.targetTableName, column.derivedColumnName,
            column.derivedColumnId, column.transformation))
          // TODO: Manish, I have replaced third option null with "". null should be avoided in scala.
          transformationMappingList += (("0", column.parentColumnId, ""))
          val parentListMap: List[MappingList] = List(MappingList("", derievedColumnId))
          columnTemp = columnTemp ++ parentListMap

          // edlLogger.logInfo("Completed",
          // s"Searching For ${column.targetTableName} & Column ${column.derivedColumnName}\n")
          // edlLogger.logInfo("", "FINISHED\n")
      }
      finalRecords = finalRecords :+ NormalizedHireDetails.apply(parentRecAdd, returnedRecords)
    })
    println("FINAL")
    var finalRes: finalOutput = finalOutput(inputTableList.toList, List(outputTableList), finalRecords)
    columnTemp = columnTemp.distinct
    val groupedByChild = columnTemp.groupBy(_.child)
    val HierInput = groupedByChild.map { case (child, items) =>
      val parents = items.map(_.parent) // Extract the list of parents
      Columns(child, removeAllOccurrences(parents, child))
    }.toList

    val jsonString = createLineage(finalRes)
    val json: JsValue = Json.parse(jsonString)
    val columnLineage = (json \ "columnLineage").get.toString() // mapping the transformation
    //edlLogger.logInfo("Json Lineage", columnLineage)

    //edlLogger.logInfo("Hire", "printAllHierarchies")
    val hierarchy = buildHierarchy(HierInput)
    printAllHierarchies(hierarchy)
    //edlLogger.logInfo("Hire", hierarchy.mkString("\n"))
    columnLineage
  }

  private def callFromParent(parentId: String, parentLists: List[String], column: ColumnLineageRecord,
    rawSQLLineage: Seq[ColumnLineageRecord], parentRec: List[NormalizedHire]): TransformMapping = {

    var NormalizedHierRecordsTemp: List[NormalizedHire] = List()
    val retVal = recursive(parentId, parentLists, column, rawSQLLineage, parentRec, column.derivedColumnId)
    val inTransformListMap = retVal.appendList
    var columnTemp: List[MappingList] = List()
    columnTemp = columnTemp ++ retVal.inMap

    NormalizedHierRecordsTemp = NormalizedHierRecordsTemp ++ retVal.inFinalOutput

    println(inTransformListMap.mkString("\n"))
    println("")
    TransformMapping(NormalizedHierRecordsTemp, inTransformListMap, columnTemp)

  }
  private def recursive(parentId: String, parentLists: List[String], column: ColumnLineageRecord,
    rawSQLLineage: Seq[ColumnLineageRecord], parentRec: List[NormalizedHire], childId: String): TransformMapping = {

    var parentRecTemp: List[NormalizedHire] = List()
    var columnTemp: List[MappingList] = List()
    val tagName = "recursiveCall"
    val appendList = ListBuffer.empty[(String, String, String)]
    val parentSize = parentLists.size
    parentLists.foreach(pcol => {

      val isRoot = parentRec.filter(p => p.LookupKey.equalsIgnoreCase(pcol))
      val index = parentLists.indexOf(pcol)
      val flag = index.equals(parentSize)

      (isRoot.size, flag) match {
        case (0, false) =>
          //edlLogger.logInfo(tagName, s"Searching For $pcol ")
          val parentListsTemp = removeElement(rawSQLLineage.filter
          (
            base => base.derivedColumnId.contains(pcol) && !base.parentColumnId.equalsIgnoreCase(pcol)).toList,
            pcol).map(_.parentColumnId).mkString.split(",")
            .distinct.toList

          parentRecTemp = recursive(parentId, parentListsTemp, column, rawSQLLineage, parentRec, pcol).inFinalOutput
          appendList += ((parentLists.indexOf(pcol).toString, pcol, column.parentColumnId))
          columnTemp = columnTemp ++ manageMapping(pcol, childId)

        case _ =>
          val mesg = isRoot.map(b => s"${b.LookupTable} && ${b.LookUpColumn}")
          appendList += ((parentLists.indexOf(pcol).toString, pcol, column.parentColumnId))
          columnTemp = columnTemp ++ manageMapping(pcol, childId)
          isRoot.map(p => {
            // TODO: Manish, I have replaced third option null with "". null should be avoided in scala.
            val normalizedLineageRecords = NormalizedHire(p.LookupTable, p.LookUpColumn, pcol, "")
            parentRecTemp = parentRecTemp :+ normalizedLineageRecords
          })
      }
    }
    )
    TransformMapping(parentRecTemp, appendList, columnTemp)
  }
  def nvl2[T](value: T, whenNotNull: T, whenNull: T): T = {
    if (value != null) whenNotNull else whenNull
  }
  private case class ColumnLineageMap(var TargetColumn: String = "NA", var SourceColumn: String = "NA",
      var SourceTable: String = "NA")

  def generateColumnLineage(sqlQuery: String, dataframe: DataFrame, inSpark: SparkSession): Unit = {
    var DL = Map[String, ColumnLineageDatasetFacetFieldsAdditional]()
    val schema = dataframe.schema.toList.asJava
    var ColMapping: Map[String, ColumnLineageMap] = Map.empty[String, ColumnLineageMap]

    var SQLQuery = sqlQuery
    var extractedLineage = columnJson(dataframe, inSpark)

    if (extractedLineage.nonEmpty) {

      val mapper: ObjectMapper = JsonMapper.builder
        .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
        .build

      var StartNum = 0
      val receivedMessage: JsonNode = mapper.readTree(extractedLineage)
      receivedMessage.forEach(element => {
        val targetColumn = element.path("descendant").get("name")
        mapper.readTree(element.get("lineage").toString).forEach(subElement => {
          ColMapping += (StartNum.toString + "#" + targetColumn.toString -> ColumnLineageMap(targetColumn.toString,
            subElement.get("name").toString, subElement.get("origin").toString))
          StartNum = StartNum + 1
        })
      })
    }

    schema.forEach { field => {
      val SearchColumn = ColMapping.values.toList.zipWithIndex.filter(pair =>
        pair._1.TargetColumn.replaceAll("\"", "").equalsIgnoreCase(field.name))

      SearchColumn.foreach { MapField =>
        print("MAP FIELS " + MapField)
      }
    }
    }
    DL.foreach(DLA => {
      print("DLA" + DLA)
    })
  }

}
