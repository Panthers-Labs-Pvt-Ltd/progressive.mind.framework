package com.progressive.minds.chimera.core.observability

trait MetricCollector {
  def collectMetric(name: String, value: Any): Unit
  def report(): Unit
}
