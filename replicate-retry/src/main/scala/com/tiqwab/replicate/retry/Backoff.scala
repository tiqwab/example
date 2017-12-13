package com.tiqwab.replicate.retry

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

case class Backoff(max: Int, delay: FiniteDuration, base: Long = 2L) extends Policy {
  require(max > 0)

  def timer: Timer = DefaultTimer

  def apply[T](f: () => Future[T])(implicit success: Success[T], ec: ExecutionContext): Future[T] = {
    def calcDuration(duration: FiniteDuration): FiniteDuration =
      duration * base
    def loop(g: () => Future[T], remains: Int, currentDuration: FiniteDuration): Future[T] =
      g()
        .flatMap {
          case x if success.predicate(x) =>
            Future.successful(x)
          case x if remains <= 0 =>
            Future.successful(x)
          case _ =>
            timer.delay(currentDuration).flatMap { _ =>
              val nextDuration = calcDuration(currentDuration)
              loop(g, remains - 1, nextDuration)
            }
        }
        .recoverWith {
          case e if remains <= 0 =>
            Future.failed(e)
          case _ =>
            timer.delay(currentDuration).flatMap { _ =>
              val nextDuration = calcDuration(currentDuration)
              loop(g, remains - 1, nextDuration)
            }
        }
    loop(f, max - 1, delay)
  }
}
