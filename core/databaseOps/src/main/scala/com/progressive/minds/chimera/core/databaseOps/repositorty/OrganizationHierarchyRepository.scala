package com.progressive.minds.chimera.core.databaseOps.repositorty

import com.progressive.minds.chimera.core.databaseOps.databaseOperations.{executeQuery, executeUpdate, extractColumnDetails, extractColumnNamesAndValues, extractFieldNames, generateWhereClause, mapToCaseClass}
import com.progressive.minds.chimera.core.databaseOps.modal.OrganizationHierarchy


object OrganizationHierarchyRepository  {
  private val originaltableNm  = "OrganizationHierarchy"
  private val primaryKey : Seq[String] = Seq("org_id")
  private var attributesLists = extractFieldNames[OrganizationHierarchy]
  private var result: List[OrganizationHierarchy] = _
  private val selectSql = s"SELECT $attributesLists FROM $originaltableNm"

  def listAllOrganizationHierarchy: List[OrganizationHierarchy] = {
    val list = executeQuery(selectSql)
    result = list.map(mapToCaseClass[OrganizationHierarchy])
    result
  }

  def listOrganizationHierarchyByColumns(requestMap: Map[String, Any]): List[OrganizationHierarchy] = {
    val whereClause = generateWhereClause(requestMap)
    val sqlByColumns = s"$selectSql " + (if (whereClause.nonEmpty) s" $whereClause" else "")
    val list = executeQuery(sqlByColumns)
    result = list.map(mapToCaseClass[OrganizationHierarchy])
    result
  }

  def editOrganizationHierarchy(orgHier : OrganizationHierarchy): Unit = {
    val columnValueMap = extractColumnDetails(orgHier)
    var mergeSQL : String = ""
    var whereCondition : String =""

    columnValueMap.foreach { case (name, dataType, value) =>
      val placeholderValue = value match {
        case s: String => s"'$s'"
        case d: java.util.Date => s"'${new java.sql.Timestamp(d.getTime)}'"
        case _ => value.toString
      }
      if (primaryKey.contains(name))
        whereCondition = whereCondition + s" $name = $placeholderValue AND"
        else
        mergeSQL = mergeSQL + s"\t\n $name = $placeholderValue,"
      }

    val SQL = s"Update $originaltableNm SET ${mergeSQL.stripSuffix(",")} WHERE ${whereCondition.stripSuffix("AND")}"
    executeUpdate(SQL)
  }

}