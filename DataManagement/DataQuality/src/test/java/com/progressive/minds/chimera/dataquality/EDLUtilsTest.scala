package com.progressive.minds.chimera.dataquality

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import java.util.Calendar
import java.util.TimeZone
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EDLUtilsTest extends AnyFlatSpec with Matchers {

  "getFileNameFromCsvPath" should "extract the file name from a given CSV path" in {
    val filePath = "/path/to/file.csv"
    val result = EDLUtils.getFileNameFromCsvPath(filePath)
    result shouldEqual "file.csv"
  }

  it should "return the file name when there is no directory in the path" in {
    val filePath = "file.csv"
    val result = EDLUtils.getFileNameFromCsvPath(filePath)
    result shouldEqual "file.csv"
  }

  "transformMetdataFields" should "replace placeholders with values from JSON" in {
    val elementValue = "Value with ${trigger/someField}"
    val eventJSON: JsonNode = JsonNodeFactory.instance.objectNode().put("someField", "replacedValue")
    val result = EDLUtils.transformMetdataFields(elementValue, eventJSON)
    result shouldEqual "Value with replacedValue"
  }

  it should "return the original value if there are no placeholders" in {
    val elementValue = "No placeholders here"
    val eventJSON: JsonNode = JsonNodeFactory.instance.objectNode()
    val result = EDLUtils.transformMetdataFields(elementValue, eventJSON)
    result shouldEqual elementValue
  }

  it should "return an empty string if the input is null or blank" in {
    val elementValue = ""
    val eventJSON: JsonNode = JsonNodeFactory.instance.objectNode()
    val result = EDLUtils.transformMetdataFields(elementValue, eventJSON)
    result shouldEqual ""
  }

  "isNullOrBlank" should "return true for null input" in {
    // scalastyle:off null
    val input: String = null
    // scalastyle:on null
    val result = EDLUtils.isNullOrBlank(input)
    result shouldEqual true
  }

  it should "return true for blank input" in {
    val input = ""
    val result = EDLUtils.isNullOrBlank(input)
    result shouldEqual true
  }

  it should "return false for non-blank input" in {
    val input = "non-blank"
    val result = EDLUtils.isNullOrBlank(input)
    result shouldEqual false
  }

  "toJsonString" should "convert a block to a JSON string" in {
    val jsonString = EDLUtils.toJsonString { generator =>
      generator.writeStartObject()
      generator.writeStringField("key", "value")
      generator.writeEndObject()
    }
    jsonString shouldEqual """{"key":"value"}"""
  }

  "convertTimeFormat" should "convert calendar time to specified format" in {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.set(2023, Calendar.JANUARY, 1, 0, 0, 0)
    val result = EDLUtils.convertTimeFormat("yyyy-MM-dd HH:mm:ss", calendar)
    result shouldEqual "2023-01-01 00:00:00"
  }

  "currentGMTCalender" should "return a calendar instance with UTC timezone" in {
    val calendar = EDLUtils.currentGMTCalender()
    calendar.getTimeZone.getID shouldEqual "UTC"
  }

  "isValidJson" should "return true for valid JSON string" in {
    val jsonString = """{"key": "value"}"""
    val result = EDLUtils.isValidJson(jsonString)
    result shouldEqual true
  }

  it should "return false for invalid JSON string" in {
    val jsonString = """{"key": "value"""
    val result = EDLUtils.isValidJson(jsonString)
    result shouldEqual false
  }

  "jsonToMapConvertor" should "convert valid JSON string to Map" in {
    val jsonString = """[{"Key": "key1", "Value": "value1"}, {"Key": "key2", "Value": "value2"}]"""
    val result = EDLUtils.jsonToMapConvertor(jsonString)
    result shouldEqual Map("key1" -> "value1", "key2" -> "value2")
  }

  it should "return an empty Map for null input" in {
    // scalastyle:off null
    val jsonString: String = null
    // scalastyle:on null
    val result = EDLUtils.jsonToMapConvertor(jsonString)
    result shouldEqual Map.empty
  }

  it should "return an empty Map for empty input" in {
    val jsonString = ""
    val result = EDLUtils.jsonToMapConvertor(jsonString)
    result shouldEqual Map.empty
  }
}
