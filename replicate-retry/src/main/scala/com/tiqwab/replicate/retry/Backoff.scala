package com.tiqwab.replicate.retry

import scala.concurrent.duration._

case class Backoff(maxRetryCount: Int, initialDelay: FiniteDuration, base: Long = 2L) extends Policy {
  override def timer: Timer = DefaultTimer
  override def calcDuration(currentDuration: FiniteDuration): FiniteDuration = currentDuration * base
}
