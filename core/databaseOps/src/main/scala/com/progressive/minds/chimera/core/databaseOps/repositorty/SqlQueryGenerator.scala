package com.progressive.minds.chimera.core.databaseOps.repositorty

import java.util.Locale
import scala.reflect.runtime.universe._

object SqlQueryGenerator {

  def getSQL[T: TypeTag](model: T, tableName: String, actionType : String = "SELECT"): String = {
    val tpe = typeOf[T]
    var retVal : String = ""
    // Get the fields of the class using reflection
    val fields = tpe.decls.collect {
      case m: MethodSymbol if m.isGetter => m.name.toString.trim
    }.toList

    val result = actionType.toLowerCase(Locale.ROOT) match {
      case "insert" =>
        val columnNames = fields.mkString(", ")
        val placeholders = fields.map(_ => "?").mkString(", ")

        retVal = s"INSERT INTO $tableName ($columnNames) VALUES ($placeholders)"
      case "update" =>
        val setClause = fields.map(f => s"$f = ?").mkString(", ")
        retVal = s"UPDATE $tableName SET $setClause WHERE id = ?"

      case "delete" =>
        val setClause = fields.map(f => s"$f = ?").mkString(", ")
        retVal = s"UPDATE $tableName SET $setClause WHERE id = ?"
      case _ =>
        val columns = fields.mkString(", ")
        retVal = s"SELECT $columns FROM $tableName"
    }
    retVal
  }
}
