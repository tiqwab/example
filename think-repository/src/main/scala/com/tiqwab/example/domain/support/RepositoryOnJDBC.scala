package com.tiqwab.example.domain.support

import scalikejdbc.DBSession

case class EntityIOContextOnJDBC(session: DBSession) extends EntityIOContext

trait RepositoryOnJDBC[ID <: Identifier[Long], E <: Entity[ID]]
  extends Repository[ID, E] {

  protected def withDBSession[A](ctx: EntityIOContext)(f: DBSession => A): A = {
    ctx match {
      case EntityIOContextOnJDBC(dbSession) => f(dbSession)
      case _ => throw new IllegalStateException(s"Unexpected context is bound (expected: JDBCEntityIOContext, actual: $ctx)")
    }
  }

}
