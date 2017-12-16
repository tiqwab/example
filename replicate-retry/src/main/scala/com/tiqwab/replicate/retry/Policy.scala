package com.tiqwab.replicate.retry

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

trait Policy {
  def timer: Timer
  def maxRetryCount: Int
  def initialDelay: FiniteDuration
  def calcDuration(currentDuration: FiniteDuration): FiniteDuration
  def apply[T](f: () => Future[T])(implicit success: Success[T], ec: ExecutionContext): Future[T] = {
    def loop(g: () => Future[T], remains: Int, delayDuration: FiniteDuration): Future[T] =
      g()
        .flatMap {
          case x if success.predicate(x) =>
            Future.successful(x)
          case x if remains <= 0 =>
            Future.successful(x)
          case _ =>
            timer.delay(delayDuration).flatMap { _ =>
              val nextDuration = calcDuration(delayDuration)
              loop(g, remains - 1, nextDuration)
            }
        }
        .recoverWith {
          case NonFatal(_) if remains > 0 =>
            timer.delay(delayDuration).flatMap { _ =>
              val nextDuration = calcDuration(delayDuration)
              loop(g, remains - 1, nextDuration)
            }
        }
    loop(f, maxRetryCount - 1, initialDelay)
  }

  require(maxRetryCount > 0)

}
