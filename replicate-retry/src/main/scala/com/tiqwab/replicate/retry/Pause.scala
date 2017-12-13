package com.tiqwab.replicate.retry

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

case class Pause(count: Int, duration: FiniteDuration) extends Policy {
  require(count > 0)

  def timer: Timer = DefaultTimer

  def apply[T](f: () => Future[T])(implicit success: Success[T], ec: ExecutionContext): Future[T] = {
    def loop(g: () => Future[T], remains: Int): Future[T] =
      g()
        .flatMap {
          case x if success.predicate(x) =>
            Future.successful(x)
          case x if remains <= 0 =>
            Future.successful(x)
          case _ =>
            timer.delay(duration).flatMap { _ =>
              loop(g, remains - 1)
            }
        }
        .recoverWith {
          case e if remains <= 0 =>
            Future.failed(e)
          case _ =>
            timer.delay(duration).flatMap { _ =>
              loop(g, remains - 1)
            }
        }
    loop(f, count - 1)
  }

}
