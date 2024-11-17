package com.progressive.minds.chimera.core.databaseOps

import com.progressive.minds.chimera.core.logger.ChimeraLogger
import java.sql.{Connection, PreparedStatement, ResultSet}
import scala.util.Using
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

object databaseOperations {
  private val edlLogger = new ChimeraLogger(this.getClass)


  // HikariCP DataSource
  private val dataSource: HikariDataSource = {
    val config = new HikariConfig()
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres") // Database URL
    config.setUsername("postgres") // Username
    config.setPassword("root") // Password
    config.setDriverClassName("org.postgresql.Driver") // JDBC Driver
    config.setMaximumPoolSize(10) // Pool size
    config.setMinimumIdle(2)
    config.setIdleTimeout(600000) // 10 minutes
    config.setMaxLifetime(1800000) // 30 minutes
    config.setConnectionTimeout(30000) // 30 seconds
    new HikariDataSource(config)
  }

  // Method to get a connection from the pool
  private def getConnection: Connection = dataSource.getConnection

  // Execute a query and return results as a List of Maps
  def executeQuery(query: String, params: Seq[Any] = Seq.empty): List[Map[String, Any]] = {
    val results = Using.resource(getConnection) { connection =>
      Using.resource(connection.prepareStatement(query)) { preparedStatement =>
        setParams(preparedStatement, params)
        val resultSet = preparedStatement.executeQuery()
        resultSetToMap(resultSet)
      }
    }

    Option(results).getOrElse {
      edlLogger.logError("executeQuery", "Failed to execute query")
      List.empty
    }
  }
  import scala.util._

  def executeUpdate(query: String, params: Seq[Any] = Seq.empty): Int = {
    Using(getConnection) { connection =>
      Using(connection.prepareStatement(query)) { preparedStatement =>
        setParams(preparedStatement, params)
        preparedStatement.executeUpdate()
      }.getOrElse(0) // Default to 0 on failure
    }.getOrElse(0) // Default to 0 on failure
  }
  // Utility to set parameters in PreparedStatement
  private def setParams(preparedStatement: PreparedStatement, params: Seq[Any]): Unit = {
    params.zipWithIndex.foreach { case (param, index) =>
      preparedStatement.setObject(index + 1, param)
    }
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
    edlLogger.logInfo("Shutting Down","Shutting down database connection pool...")
    dataSource.close()
  }

  // Main method for testing
  def main(args: Array[String]): Unit = {
    val selectQuery = "SELECT ORG_ID,PARENT_ORG_ID,ORG_TYPE_ID,ORG_NAME,COO_OWNER,OPS_LEAD,TECH_LEAD,BUS_OWNER,ORG_DESC,ORG_EMAIL,ORG_CI,USER_FIELD_1,USER_FIELD_2," +
      "USER_FIELD_3,USER_FIELD_4,USER_FIELD_5,STATUS,INSERTED_USER,INSERT_DT,MODIFIED_BY,UPDATE_DT" +
      "FROM OrganizationHierarchy WHERE ORG_ID = ? AND ORG_NAME= ?"
    val selectParams = Seq("1", "Natwest")

    edlLogger.logInfo("Database Manager",s"Executing query: $selectQuery with params: $selectParams")
    val results = executeQuery(selectQuery, selectParams)

    edlLogger.logInfo("Database Manager",s"Total Records Found: ${results.size}")
    results.foreach(row => println(row.toString()))

    val updateQuery = "UPDATE OrganizationHierarchy SET INSERTED_USER = ? WHERE ORG_ID = ?"
    val updateParams = Seq("kumamjf", 1)

    edlLogger.logInfo("Database Manager",s"Executing update: $updateQuery with params: $updateParams")
    val rowsUpdated = executeUpdate(updateQuery, updateParams)
    edlLogger.logInfo("Database Manager",s"Rows Updated: $rowsUpdated")
  }
}
