package models.authentication.lifecycle

import models.{Entity, Iso}
import models.authentication.{Email, User, UserId}
import scalikejdbc.{AsIsParameterBinder, DBSession, WrappedResultSet}
import scalikejdbc.interpolation.SQLSyntax
import skinny.orm.{Alias, SkinnyCRUDMapperWithId}

class UserRepositoryOnJdbc(override val tableName: String)(
    implicit val iso: Iso[Long, UserId])
    extends UserRepository[DBSession]
    with SkinnyCRUDMapperWithId[UserId, User]
    with CRUDMapperWithId[UserId, User] {

  override def defaultAlias: Alias[User] =
    createAlias("USER")

  def namedValues(entity: User): Seq[(SQLSyntax, Any)] = Seq(
    column.id -> AsIsParameterBinder(idToRawValue(entity.id)),
    column.email -> entity.email.value,
    column.createdAt -> entity.createdAt
  )

  override def extract(rs: WrappedResultSet,
                       n: scalikejdbc.ResultName[User]): User = {
    User(
      id = rs.get(n.id),
      email = Email(rs.get(n.email)),
      createdAt = rs.get(n.createdAt)
    )
  }

}
