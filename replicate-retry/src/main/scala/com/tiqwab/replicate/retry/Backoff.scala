package com.tiqwab.replicate.retry

import odelay.Timer.default

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

case class Backoff(max: Int, delay: FiniteDuration, base: Int = 2) {
  require(max > 0)

  def apply[T](f: () => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    def calcDuration(duration: FiniteDuration): FiniteDuration =
      duration * base
    def loop[T](g: () => Future[T], remains: Int, currentDuration: FiniteDuration): Future[T] =
      g().recoverWith {
        case e if remains <= 0 =>
          Future.failed(e)
        case _ =>
          odelay.Delay(currentDuration)(()).future.flatMap { _ =>
            val nextDuration = calcDuration(currentDuration)
            loop(g, remains - 1, nextDuration)
          }
      }
    loop(f, max - 1, delay)
  }
}
