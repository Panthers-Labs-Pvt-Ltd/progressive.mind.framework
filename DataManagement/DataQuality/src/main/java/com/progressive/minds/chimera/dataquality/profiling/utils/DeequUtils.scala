package com.progressive.minds.chimera.dataquality.profiling.utils

import com.amazon.deequ.VerificationResult
import com.amazon.deequ.repository.{MetricsRepository, ResultKey}
import org.apache.spark.sql.DataFrame

import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import scala.util.Try

object DeequUtils {
  type Verifier = (DataFrame, MetricsRepository, ResultKey) => VerificationResult
  type VerifierNoRepo = DataFrame => VerificationResult

  def executeRulesNoRepo(rulesDf: DataFrame,
                         deequTargetDf: DataFrame): VerificationResult ={
    val verificationResult: VerifierNoRepo = getVerifierNoRepo(rulesDf).get
    verificationResult(deequTargetDf)
  }

  private def getRuleForVerifier(rulesDf: DataFrame, checkLevel: String): List[(String, Long)] ={
    val rules = rulesDf.select("rowNum", "ruleValue")
      .where(s"checkLevel = '$checkLevel'")
      .collect()
    rules.map(row=> {
      (row.getString(1), row.getInt(0).toLong)
    }).toList
  }

  def getVerifierNoRepo(rulesDf: DataFrame): Try[VerifierNoRepo] = {
    val constraintWarningCheckCodes = getRuleForVerifier(rulesDf, "Warning")
    val constraintErrorCheckCodes = getRuleForVerifier(rulesDf, "Error")

    def checkWarningSrcCode (checkCodeMethod: String, id: Long): String = {
      s"""com.amazon.deequ.checks.Check(com.amazon.deequ.checks.CheckLevel.Warning, "$id")$checkCodeMethod"""
      }

    def checkErrorSrcCode(checkCodeMethod: String, id: Long): String = {
      s"""com.amazon.deequ.checks.Check(com.amazon.deequ.checks.CheckLevel.Error, "$id")$checkCodeMethod"""
    }
    val verifierSrcCode =
    s"""{
        |import com.amazon.deequ.constraints.ConstrainableDataTypes
        |import com.amazon.deequ.(VerificationResult, VerificationSuite)
        |import org.apache.spark.sql.DataFrame
        |import com.amazon.deequ.analyzers.Size
        |
        |val warningChecks = Seq(
        |${
      constraintWarningCheckCodes.map {
        (checkWarningSrcCode _).tupled
      }.mkString(", \n ")
    }
      |)
      |val errorChecks = Seq(
      |${
      constraintErrorCheckCodes.map {
        (checkErrorSrcCode _).tupled
      }.mkString(",\n ")
    }
    |)
    |
    |(deequTargetdf: DataFrame) => VerificationSuite()
    |.onData(deequTargetDf)
    |.addChecks(warningChecks)
    |.addChecks(errorChecks)
    |.run()
    |}
    """.stripMargin.trim
    compile[VerifierNoRepo](verifierSrcCode)

}

  def compile[T](source:String):Try[T]=
    Try {
    val toolBox = currentMirror.mkToolBox()
    val tree = toolBox.parse(source)
    val compiledCode = toolBox.compile(tree)
    compiledCode().asInstanceOf[T]
    }
}