package com.progressive.minds.chimera.capabilities.DataQuality.entities

object DeequRunnerProcessStatus extends Enumeration {
  type Main = Value
  val Error: Value = Value("ERROR")
  val Warning: Value = Value("WARNING")
  val Success: Value = Value("SUCCESS")
  val Unknown: Value = Value("UNKNOWN")

}
