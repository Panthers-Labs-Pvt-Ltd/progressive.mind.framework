package com.progressive.minds.chimera.core.databaseOps.apacheComet


import org.apache.comet.config.Settings


import org.apache.comet.extractor.Main.settings
import org.apache.comet.extractor.Main.settings.storageHandler
import org.apache.comet.schema.handlers.{SchemaHandler, StorageHandler}
import org.apache.comet.job.ingest.IngestionJob
import org.apache.comet.job.metrics.MetricsJob
import org.apache.comet.schema.model.{Domain, Schema, Type}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.comet.schema.handlers.SchemaHandler
import org.apache.comet.job.ingest.IngestionJob
import org.apache.comet.schema.model.{Domain, Schema}
import org.apache.hadoop.fs.Options.HandleOpt.path
import org.apache.hadoop.fs.{ContentSummary, FSDataOutputStream, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp

import java.time.LocalDateTime
import java.util.regex.Pattern
import scala.concurrent.duration.FiniteDuration
import scala.util.Try
object apacheComet extends IngestionJob() {

  val spark = SparkSession.builder()
    .appName("Apache Comet Integration")
    .config("spark.master", "local[*]") // Adjust for your cluster
    .config("comet.config.file", "path/to/application.conf") // Point to the config file
    .getOrCreate()

  val storageHandler = new StorageHandler() {
    override def move(src: Path, dst: Path): Boolean = ???

    override def delete(path: Path): Boolean = ???

    override def exists(path: Path): Boolean = ???

    override def mkdirs(path: Path): Boolean = ???

    override def copyFromLocal(source: Path, dest: Path): Unit = ???

    override def moveFromLocal(source: Path, dest: Path): Unit = ???

    override def moveSparkPartFile(sparkFolder: Path, extension: String): Option[Path] = ???

    override def read(path: Path): String = ???

    override def write(data: String, path: Path): Unit = ???

    override def listDirectories(path: Path): List[Path] = ???

    override def list(path: Path, extension: String, since: LocalDateTime, recursive: Boolean, exclude: Option[Pattern]): List[Path] = ???

    override def blockSize(path: Path): Timestamp = ???

    override def contentSummary(path: Path): ContentSummary = ???

    override def lastModified(path: Path): Timestamp = ???

    override def spaceConsumed(path: Path): Timestamp = ???

    override def getOutputStream(path: Path): FSDataOutputStream = ???

    override def touchz(path: Path): Try[Unit] = ???

    override def touch(path: Path): Try[Unit] = ???

    override def lockAcquisitionPollTime: FiniteDuration = ???

    override def lockRefreshPollTime: FiniteDuration = ???

    override def unzip(source: Path, targetDir: Path): Try[Unit] = ???
  }
  val schemaHandler = new SchemaHandler(storage = storageHandler)
  val domain = schemaHandler.getDomain("default").get // Replace "default" with your domain name
  val schema = schemaHandler.getSchema(domain.name, "users").get // Replace "users" with your schema name

  import com.typesafe.config.ConfigFactory

  val config = ConfigFactory.load() // Loads application.conf from resources
  val metadataRoot = config.getString("comet.metadata.root")
  println(s"Metadata root directory: $metadataRoot")

  val inputPaths = List(new Path("/path/to/input/users.csv"))


  implicit val settings: Settings = {
    val config = ConfigFactory.load() // Loads application.conf
    Settings(config) // Initializes Settings with the loaded configuration
  }

  val ingestionJob = new IngestionJob (domain, schema, null, schemaHandler, null, null, null ,null,null, null)


}

