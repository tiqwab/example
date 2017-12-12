package com.tiqwab.replicate.retry

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

case class Pause(count: Int, duration: FiniteDuration) extends Policy {
  require(count > 0)

  def timer: Timer = DefaultTimer

  def apply[T](f: () => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    def loop[T](g: () => Future[T], remains: Int): Future[T] =
      g().recoverWith {
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
