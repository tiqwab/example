package models

import scalikejdbc.{DB, DBSession}

import scala.concurrent.{ExecutionContext, Future}

trait JDBCContextCreator extends ContextCreator[DBSession] {

  override def futureContext[A](execution: (DBSession) => Future[A])(
      implicit ec: ExecutionContext): Future[A] =
    DB.futureLocalTx(execution)

  override def syncContext[A](execution: (DBSession) => A): A =
    DB.localTx(execution)

}

object JDBCContextCreator extends JDBCContextCreator
