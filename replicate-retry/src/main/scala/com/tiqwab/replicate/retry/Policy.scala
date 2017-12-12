package com.tiqwab.replicate.retry

import scala.concurrent.{ExecutionContext, Future}

trait Policy {
  def timer: Timer
  def apply[T](f: () => Future[T])(implicit ec: ExecutionContext): Future[T]
}
