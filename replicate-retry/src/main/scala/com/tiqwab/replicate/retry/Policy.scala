package com.tiqwab.replicate.retry

import scala.concurrent.{ExecutionContext, Future}

trait Policy {
  def timer: Timer
  def apply[T](f: () => Future[T])(implicit success: Success[T], ec: ExecutionContext): Future[T]
}
