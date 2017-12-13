package com.tiqwab.replicate.retry

import scala.concurrent.duration._

case class Pause(maxRetryCount: Int, initialDelay: FiniteDuration) extends Policy {
  override def timer: Timer = DefaultTimer
  override def calcDuration(currentDuration: FiniteDuration): FiniteDuration = currentDuration
}
