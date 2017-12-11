package com.tiqwab.replicate.retry

import odelay.Timer.default

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

case class Pause(count: Int, duration: FiniteDuration) {
  require(count > 0)

  def apply[T](f: () => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    def loop[T](g: () => Future[T], remains: Int): Future[T] =
      g().recoverWith {
        case e if remains <= 0 =>
          Future.failed(e)
        case _ =>
          odelay.Delay(duration)(()).future.flatMap { _ =>
            loop(g, remains - 1)
          }
      }
    loop(f, count - 1)
  }

}
