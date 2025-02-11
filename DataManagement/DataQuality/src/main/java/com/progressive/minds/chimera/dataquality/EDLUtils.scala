package com.progressive.minds.chimera.dataquality

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.{HashMap, List}
import scala.jdk.CollectionConverters._
import com.fasterxml.jackson.core.{JsonEncoding, JsonGenerator}
import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

import scala.util.matching.Regex

object EDLUtils {
  private val mapper = new ObjectMapper().registerModule(DefaultScalaModule)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  private val edlLogger = ChimeraLoggerFactory.getLogger(this.getClass)

  def getFileNameFromCsvPath(filePath: String): String = {
    val tableName: String = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length)
    tableName
  }

  def transformMetdataFields(elementValue: String, eventJSON: JsonNode): String = {
    var returnValue = ""
    if (!isNullOrBlank(elementValue)) {
      returnValue = elementValue
      val stringPattern: Regex = """\$\{trigger\/(.*?)}""".r
      if (elementValue.contains("${trigger/")) {
        val patternMatches = stringPattern.findAllMatchIn(elementValue).toList
        for {curMatch <- patternMatches} {
          val replacingString = eventJSON.at(curMatch.toString().substring(9, curMatch.toString().length - 1)).toString
            .replace("\"", "")
          returnValue = returnValue.replace(curMatch.toString(), replacingString)
        }
      }
    }
    returnValue
  }

  def isNullOrBlank(i: String): Boolean = i match {
    case null => true
    case "" => true
    case _ => false
  }

  def toJsonString(block: JsonGenerator => Unit): String = {
    val baos = new ByteArrayOutputStream()
    val generator = mapper.createGenerator(baos, JsonEncoding.UTF8)
    block(generator)
    generator.close()
    baos.close()
    new String(baos.toByteArray, StandardCharsets.UTF_8)
  }

  def classForName[C](className: String, initialize: Boolean = true, noSparkClassLoader: Boolean = false): Class[C] = {
    if (!noSparkClassLoader) {
      Class.forName(className, initialize, getContextOrSparkClassLoader).asInstanceOf[Class[C]]
    }
    else {
      Class.forName(className, initialize, Thread.currentThread().getContextClassLoader).asInstanceOf[Class[C]]
    }
  }

  def getContextOrSparkClassLoader: ClassLoader =
    Option(Thread.currentThread().getContextClassLoader).getOrElse(getSparkClassLoader)

  def getSparkClassLoader: ClassLoader = getClass.getClassLoader

  def merge(obj: Any, update: Any): Any = {
    if (!obj.getClass.isAssignableFrom(update.getClass)) return obj
    val methods = obj.getClass.getMethods
    for {fromMethod <- methods} {
      if (fromMethod.getDeclaringClass == obj.getClass && fromMethod.getName.startsWith("get")) {
        val fromName = fromMethod.getName
        val toName = fromName.replaceFirst("get", "set")
        try {
          val toMethod = obj.getClass.getMethod(toName, fromMethod.getReturnType)
          val value = fromMethod.invoke((update))
          if (value != null) toMethod.invoke(obj, value)
        } catch {
          case e: Exception =>
        }
      }
    }
    obj
  }

  def convertTimeFormat(timeFormat: String, calender: Calendar): String = {
    val format = new SimpleDateFormat(timeFormat)
    format.setTimeZone(TimeZone.getTimeZone("Europe/London"))
    format.format(calender.getTime())
  }

  def currentGMTCalender(): Calendar = {
    val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
    Calendar.getInstance(timeZone)
  }

  def isValidJson(jsonString: String): Boolean = {
    val objectMapper = new ObjectMapper()
    try {
      objectMapper.readTree(jsonString)
      true
    }
    catch {
      case _: Throwable => false
    }
  }
  def jsonToMapConvertor(inJsonConf: String): Map[String, String] = {
    if (inJsonConf != null && inJsonConf.trim.nonEmpty) {
      val userConf: List[HashMap[String, String]] = new Gson().fromJson(
        inJsonConf,
        new TypeToken[List[HashMap[String, String]]]() {}.getType
      )

      userConf.asScala.map(item => item.asScala("Key") -> item.asScala("Value")).toMap
    } else {
      Map.empty
    }
  }
}
