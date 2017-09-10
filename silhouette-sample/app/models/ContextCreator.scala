package models

import scala.concurrent.{ExecutionContext, Future}

trait ContextCreator[C] {

  def futureContext[A](execution: C => Future[A])(
      implicit ec: ExecutionContext): Future[A]

  def syncContext[A](execution: C => A): A

}
