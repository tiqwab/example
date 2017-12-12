package com.tiqwab.replicate.retry

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait Timer {
  def delay(duration: FiniteDuration): Future[Unit]
}

object DefaultTimer extends Timer {
  override def delay(duration: FiniteDuration): Future[Unit] = {
    import odelay.Timer.default
    odelay.Delay(duration)(()).future
  }
}
