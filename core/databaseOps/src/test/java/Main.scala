package test

import com.progressive.minds.chimera.core.databaseOps.modal.OrganizationHierarchy
import com.progressive.minds.chimera.core.databaseOps.repositorty.SqlQueryGenerator

import scala.Console.println

object Main extends App {
  val person1 = OrganizationHierarchy

  val selectSql = SqlQueryGenerator.getSQL(person1, "OrganizationHierarchy")

  //val selectSql = SqlQueryGenerator.getSQL(employee, "Employee")
  println(selectSql)  // Output: INSERT INTO Employee (id, name, age, sala

  // Generate an INSERT SQL query
  val insertSql = SqlQueryGenerator.getSQL(person1, "OrganizationHierarchy", "insert")
  println(insertSql)  // Output: INSERT INTO Employee (id, name, age, salary) VALUES (?, ?, ?, ?)

  // Generate an UPDATE SQL query
  val updateSql = SqlQueryGenerator.getSQL(person1, "OrganizationHierarchy","update")
    println(updateSql)

  // Generate a DELETE SQL query
  val deleteSql = SqlQueryGenerator.getSQL(person1, "OrganizationHierarchy","delete")
  println(deleteSql)  // Output: DELETE FROM Employee WHERE id = ?
}
