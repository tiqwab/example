package models.authentication.lifecycle

import models.Iso
import models.authentication.{Email, RegistrationToken, RegistrationTokenId}
import scalikejdbc.interpolation.SQLSyntax
import scalikejdbc.{AsIsParameterBinder, DBSession, WrappedResultSet}
import skinny.orm.{Alias, SkinnyCRUDMapperWithId}

class RegistrationTokenRepositoryOnJdbc(override val tableName: String)(
    implicit val iso: Iso[String, RegistrationTokenId]
) extends RegistrationTokenRepository[DBSession]
    with SkinnyCRUDMapperWithId[RegistrationTokenId, RegistrationToken]
    with CRUDMapperWithId[RegistrationTokenId, RegistrationToken] {

  override def defaultAlias: Alias[RegistrationToken] =
    createAlias("RT")

  def namedValues(entity: RegistrationToken): Seq[(SQLSyntax, Any)] =
    Seq(
      column.id -> AsIsParameterBinder(idToRawValue(entity.id)),
      column.email -> entity.email.value,
      column.hasher -> entity.hasher,
      column.password -> entity.password,
      column.salt -> entity.salt,
      column.createdAt -> entity.createdAt,
      column.expiresAt -> entity.expiresAt
    )

  override def extract(
      rs: WrappedResultSet,
      n: scalikejdbc.ResultName[RegistrationToken]): RegistrationToken =
    RegistrationToken(
      id = rs.get(n.id),
      email = Email(rs.get(n.email)),
      hasher = rs.get(n.hasher),
      password = rs.get(n.password),
      salt = rs.get(n.salt),
      createdAt = rs.get(n.createdAt),
      expiresAt = rs.get(n.expiresAt)
    )

}
