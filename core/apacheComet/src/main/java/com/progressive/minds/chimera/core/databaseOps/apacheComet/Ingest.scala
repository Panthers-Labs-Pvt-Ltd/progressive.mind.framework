package com.progressive.minds.chimera.core.databaseOps.apacheComet

import com.ebiznext.comet.config.Settings
import com.ebiznext.comet.job.ingest.IngestionJob
import com.ebiznext.comet.schema.handlers.{SchemaHandler, StorageHandler}
import com.ebiznext.comet.schema.model.{Domain, Schema, Type}
import com.ebiznext.comet.utils.JobResult
import org.apache.hadoop.fs.Path
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

import scala.util.Try

class IngestionTemplate(
                      schema: Schema,
                      schemaHandler: SchemaHandler,
                      storageHandler: StorageHandler
                    ) extends IngestionJob {
  val domainPath = "/path/to/metadata/domains/users.yml"

  val domain: Domain = schemaHandler.loadDomain(domainPath).get


  override def schema: Schema = schema

  override def storageHandler: StorageHandler = storageHandler

  override def schemaHandler: SchemaHandler = ???

  override def types: List[Type] = ???

  override def path: List[Path] = ???

  override def options: Map[String, String] = ???

  override protected def loadDataSet(): Try[DataFrame] = ???

  override protected def ingest(dataset: DataFrame): (RDD[_], RDD[_]) = ???

  override def name: String = ???

  override implicit def settings: Settings = ???

  override def run(): Try[JobResult] = {
    println(s"Ingesting data for schema: ${schema.name}")
  }

}
