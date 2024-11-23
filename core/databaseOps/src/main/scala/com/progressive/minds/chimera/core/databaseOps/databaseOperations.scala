package com.progressive.minds.chimera.core.databaseOps

import com.progressive.minds.chimera.core.logger.ChimeraLogger
import com.progressive.minds.chimera.core.logger.ChimeraLogger.getStackTraceString
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.yaml.snakeyaml.Yaml

import java.io.{File, FileInputStream, InputStream}
import scala.reflect.runtime.universe._
import java.sql.{Connection, PreparedStatement, ResultSet}
import scala.util.Try

object databaseOperations {
  private val edlLogger = new ChimeraLogger(this.getClass)
  private val loggerTag = "databaseOperations"

  private val dataSource: HikariDataSource = {
    val environmentFilePath = Option(getClass.getClassLoader.getResource("environments.yaml"))
      .map(_.getPath)
      .getOrElse {
        edlLogger.logError(loggerTag, "File 'environments.yaml' not found.")
        throw new IllegalStateException("Missing configuration file: environments.yaml")
      }

    val inputStream: Option[InputStream] = if (new File(environmentFilePath).exists()) {
      edlLogger.logInfo(loggerTag, s"Loading configuration from: $environmentFilePath")
      Some(new FileInputStream(new File(environmentFilePath)))
    } else {
      edlLogger.logError(loggerTag, s"File does not exist: $environmentFilePath")
      None
    }

    val yaml = new Yaml()
    val data = inputStream.map(is => yaml.load(is).asInstanceOf[java.util.Map[String, Any]]).getOrElse {
      edlLogger.logError(loggerTag, "Failed to load configuration.")
      throw new IllegalStateException("Invalid or missing configuration data")
    }

    val rdsProperties = Option(data.get("localrds")).map(_.asInstanceOf[java.util.Map[String, String]])
      .getOrElse {
        edlLogger.logError(loggerTag, "Missing 'localrds' configuration.")
        throw new IllegalStateException("Missing 'localrds' configuration in YAML file")
      }

    val config = new HikariConfig()
    config.setJdbcUrl(rdsProperties.get("JdbcUrl"))
    config.setUsername(rdsProperties.get("Username"))
    config.setPassword(rdsProperties.get("Password"))
    config.setDriverClassName(rdsProperties.getOrDefault("DriverClassName", "org.postgresql.Driver"))
    config.setMaximumPoolSize(rdsProperties.getOrDefault("MaximumPoolSize", "10").toInt)
    config.setMinimumIdle(rdsProperties.getOrDefault("MinimumIdle", "2").toInt)
    config.setIdleTimeout(rdsProperties.getOrDefault("IdleTimeout", "600000").toLong)
    config.setMaxLifetime(rdsProperties.getOrDefault("MaxLifetime", "1800000").toLong)
    config.setConnectionTimeout(rdsProperties.getOrDefault("ConnectionTimeout", "30000").toLong)

    new HikariDataSource(config)
  }

  // Method to get a connection from the pool
  private def getConnection: Connection = dataSource.getConnection

  // Execute a query and return results as a List of Maps
  def executeQuery(query: String, params: Seq[Any] = Seq.empty): List[Map[String, Any]] = {
    try {
    manageResource(getConnection) { connection =>
      manageResource(connection.prepareStatement(query)) { preparedStatement =>
        setParams(preparedStatement, params)
        val resultSet = preparedStatement.executeQuery()
        resultSetToMap(resultSet)
      }.getOrElse(List.empty)
    }.getOrElse(List.empty)
  }
    catch {
      case ex: Exception =>
        edlLogger.logError("executeQuery", s"Error executing query: $query with params: $params. Exception: ${ex.getMessage}")
        List.empty
    }
  }
  def generateWhereClause(requestMap: Map[String, Any]): String = {
    if (requestMap.isEmpty) {
      ""
    } else {
      val conditions = requestMap.map {
        case (key, value) =>
          // Format the value based on its type
          value match {
            case str: String => s"$key = '$str'" // For strings, surround with single quotes
            case num: Number => s"$key = $num" // For numbers, use raw value
            case _ => s"$key = $value" // Default case (for other types)
          }
      }.mkString(" WHERE ", " AND ", "")
      conditions
    }
  }
  // Execute an update query
  def executeUpdate(query: String, params: Seq[Any] = Seq.empty): Int = {
    try {manageResource(getConnection) { connection =>
      manageResource(connection.prepareStatement(query)) { preparedStatement =>
        setParams(preparedStatement, params)
        preparedStatement.executeUpdate()
      }.getOrElse(0)
    }.getOrElse(0)
  }
    catch {
      case ex: Exception =>
        edlLogger.logError("executeUpdate", s"Error executing update: $query with params: $params. Exception: ${ex.getMessage}")
        0
    }
  }
/*

  def mapToCaseClass[T: TypeTag](map: Map[String, Any]): T = {
    val mirror = runtimeMirror(getClass.getClassLoader)
    val classSymbol = typeOf[T].typeSymbol.asClass
    val classMirror = mirror.reflectClass(classSymbol)
    val constructor = typeOf[T].decl(termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructor)

    val args = constructor.paramLists.flatten.map { param =>
      map(param.name.toString)
    }

    constructorMirror(args: _*).asInstanceOf[T]
  }
  def mapToCaseClass[T](data: Map[String, Any], constructor: Seq[Any] => T): T =
    constructor(data.values.toSeq)
*/

  // Utility to manage resources
  private def manageResource[R <: AutoCloseable, A](resource: R)(code: R => A): Try[A] = {
    Try(code(resource)).transform(
      result => {
        resource.close()
        Try(result)
      },
      exception => {
        edlLogger.logError("Database Manager", s"Error Occured: ${getStackTraceString(exception)}")
        resource.close()
        Try(throw exception)
      }
    )
  }

  // Utility to set parameters in PreparedStatement
  private def setParams(preparedStatement: PreparedStatement, params: Seq[Any]): Unit = {
    params.zipWithIndex.foreach { case (param, index) =>
      preparedStatement.setObject(index + 1, param)
    }
    edlLogger.logInfo("Database Manager", s"Executing Resolved Query: $preparedStatement")

  }

  // Utility to convert ResultSet to List of Maps
  private def resultSetToMap(resultSet: ResultSet): List[Map[String, Any]] = {
    val metaData = resultSet.getMetaData
    val columnCount = metaData.getColumnCount
    Iterator
      .continually(resultSet)
      .takeWhile(_.next())
      .map { _ =>
        (1 to columnCount).map { i =>
          metaData.getColumnName(i) -> resultSet.getObject(i)
        }.toMap
      }.toList
  }

  // Shutdown hook to close the connection pool
  sys.addShutdownHook {
    edlLogger.logInfo("databaseOperations", "Shutting down database connection pool...")
    dataSource.close()
  }

  def extractFieldNames[T: TypeTag]: String = {
    val tpe = typeOf[T]
    val members = tpe.decls.collect {
      case m: TermSymbol if m.isVal || m.isVar => m.name.toString
    }.toList
    members.mkString(",")
  }
  /*// OrganizationHierarchyRepository method for testing
  def main(args: Array[String]): Unit = {
    val selectQuery = """
      SELECT ORG_ID, ORG_NAME
      FROM OrganizationHierarchy
      WHERE ORG_ID = ? AND ORG_NAME = ?
    """
    val selectParams = Seq("1", "Natwest")
    val model : OrganizationHierarchy =  OrganizationHierarchy(_orgId= new BigInteger(1.toString), _parentOrgId="0",
      _orgTypeId=new BigInteger(1.toString), _orgName="OrgName", _cooOwner="CooOwner", _opsLead="OpsLead",
      _techLead="TechLead", _busOwner="BusOwner", _orgDesc="OrgDesc", _orgEmail="OrgEmail",
      _orgCi="OrgCi", _status="Status", _insertedUser="InsertedUser", _insertDt=null, _modifiedBy="ModifiedBy", _updateDt=null)

    val insertSql = generateInsertSql(OrganizationHierarchy)

    edlLogger.logInfo("Database Manager", s"Executing query: $selectQuery with params: $selectParams")
    val results = executeQuery(selectQuery, selectParams)

    edlLogger.logInfo("Database Manager", s"Total Records Found: ${results.size}")
    results.foreach(row => println(row))

    val updateQuery = "UPDATE OrganizationHierarchy SET INSERTED_USER = ? WHERE ORG_ID = ?"
    val updateParams = Seq("kumamjf", 1)

    edlLogger.logInfo("Database Manager", s"Executing update: $updateQuery with params: $updateParams")
    val rowsUpdated = executeUpdate(updateQuery, updateParams)
    edlLogger.logInfo("Database Manager", s"Rows Updated: $rowsUpdated")
  }

  import scala.reflect.runtime.universe._

  def generateInsertSql[T: TypeTag](model: T): String = {
    val tpe = typeOf[T]

    val tableName = tpe.typeSymbol.name.toString

    val constructor = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get

    val fieldNames = constructor.paramLists.flatten.map(_.name.toString)

    // Get the values from the case class instance
    val values = fieldNames.map { fieldName =>
      val fieldValue = model.getClass.getDeclaredField(fieldName).get(model)
      s"'$fieldValue'" // Add quotes to string values for SQL
    }

    // Construct the SQL query
    val columns = fieldNames.mkString(", ")
    val valueString = values.mkString(", ")

    s"INSERT INTO $tableName ($columns) VALUES ($valueString)"
  }
*/
  import scala.reflect.runtime.universe._
  import scala.reflect.ClassTag

  def mapToCaseClass[T: TypeTag : ClassTag](map: Map[String, Any]): T = {
    val mirror = runtimeMirror(getClass.getClassLoader)
    val classSymbol = typeOf[T].typeSymbol.asClass
    val classMirror = mirror.reflectClass(classSymbol)
    val constructor = typeOf[T].decl(termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructor)

    // Extract arguments for the constructor with type conversion
    val args = constructor.paramLists.flatten.map { param =>
      val paramName = param.name.toString
      val paramType = param.typeSignature


      // Attempt to map value and convert it to the required type
      map.get(paramName).map { value =>
        convertValueToType(value, paramType)
      }.getOrElse {
        // Handle default values if the field is optional or has a default
        if (param.asTerm.isParamWithDefault) {
          val companion = typeOf[T].typeSymbol.companion.asModule
          val companionInstance = mirror.reflectModule(companion).instance
          val defaultMethodName = TermName(s"apply$$default$$${constructor.paramLists.flatten.indexOf(param) + 1}")
          mirror.reflect(companionInstance).reflectMethod(typeOf[T].companion.decl(defaultMethodName).asMethod).apply()
        } else {
          throw new IllegalArgumentException(s"Missing required parameter: $paramName")
        }
      }
    }

    // Invoke constructor with arguments
    constructorMirror(args: _*).asInstanceOf[T]
  }

  def convertValueToType(value: Any, targetType: Type): Any = {
    if (value == null) {
      null
    } else {
      targetType match {
        case t if t =:= typeOf[String]     => value.toString
        case t if t =:= typeOf[Int]        => value.toString.toInt
        case t if t =:= typeOf[Long]       => value.toString.toLong
        case t if t =:= typeOf[Double]     => value.toString.toDouble
        case t if t =:= typeOf[Boolean]    => value.toString.toBoolean
        case t if t =:= typeOf[BigInt]     => BigInt(value.toString)
        case t if t =:= typeOf[BigDecimal] => BigDecimal(value.toString)
        case t if t =:= typeOf[java.math.BigInteger] =>
          value match {
            case v: java.math.BigInteger => v
            case v: BigInt               => v.bigInteger
            case v: String               => new java.math.BigInteger(v)
            case v: Int                  => java.math.BigInteger.valueOf(v.toLong)
            case v: Long                 => java.math.BigInteger.valueOf(v)
            case _ => throw new IllegalArgumentException(s"Cannot convert $value to java.math.BigInteger")
          }
        case t if t =:= typeOf[java.sql.Timestamp] =>
          value match {
            case v: java.sql.Timestamp => v
            case v: java.util.Date     => new java.sql.Timestamp(v.getTime)
            case v: String =>
              try {
                // Try to parse as a full timestamp format
                java.sql.Timestamp.valueOf(v)
              } catch {
                case _: IllegalArgumentException =>
                  // If it fails, parse as a date and convert to timestamp
                  val localDate = java.time.LocalDate.parse(v)
                  java.sql.Timestamp.valueOf(localDate.atStartOfDay())
              }
            case _ => throw new IllegalArgumentException(s"Cannot convert $value to java.sql.Timestamp")
          }
        case t if t =:= typeOf[Option[_]] =>
          val innerType = targetType.typeArgs.head
          Some(convertValueToType(value, innerType))
        case _ => throw new IllegalArgumentException(s"Unsupported type: $targetType")
      }
    }
  }
  // Method to extract column names and values from a case class instance
  def extractColumnNamesAndValues[T: TypeTag](caseClassInstance: T): Map[String, Any] = {
    // Runtime mirror to reflect on the class
    val mirror = runtimeMirror(getClass.getClassLoader)
    val classSymbol = typeOf[T].typeSymbol.asClass
    val classMirror = mirror.reflectClass(classSymbol)

    // Get the constructor of the case class
    val constructor = typeOf[T].decl(termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructor)

    // Get field names (column names) in the order they are defined in the case class
    val fieldNames = typeOf[T].members.collect {
      case m: MethodSymbol if m.isCaseAccessor => m.name.toString
    }.toList

    // Ensure caseClassInstance is a product (case class instance)
    val values = caseClassInstance match {
      case product: Product => product.productIterator.toList
      case _ => throw new IllegalArgumentException("The provided instance is not a case class")
    }

    // Debug: To check that field names and values align correctly
    println(s"Field Names: $fieldNames")
    println(s"Values: $values")

    // Ensure field names and values match in order, zip them together to create the map
    if (fieldNames.size != values.size) {
      throw new IllegalStateException("Mismatch between field names and values")
    }

    fieldNames.zip(values).toMap
  }

  def extractColumnDetails[T: TypeTag](caseClassInstance: T): Seq[(String, String, Any)] = {
    val tpe = typeOf[T]

    // Collect only the case accessor methods (getters for the fields) and avoid synthetic methods
    val members = tpe.decls.collect {
      case m: MethodSymbol if m.isCaseAccessor && m.isPublic => m
    }.toList

    // Extract column name (field name), data type and value
    members.map { member =>
      val fieldName = member.name.toString
      val fieldType = member.returnType.toString
      val fieldValue = caseClassInstance.getClass.getMethod(member.name.toString).invoke(caseClassInstance)
      (fieldName.split("\\$").head, fieldType, fieldValue)
    }
  }
}
