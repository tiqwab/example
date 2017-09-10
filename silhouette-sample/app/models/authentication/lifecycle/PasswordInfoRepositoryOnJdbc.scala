package models.authentication.lifecycle

import models.Iso
import models.authentication.{
  LoginInfoId,
  PasswordInfo,
  PasswordInfoId,
  PasswordInfoImpl
}
import scalikejdbc.interpolation.SQLSyntax
import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapperWithId}

class PasswordInfoRepositoryOnJdbc(override val tableName: String)(
    implicit val iso: Iso[Long, PasswordInfoId]
) extends PasswordInfoRepository[DBSession]
    with SkinnyCRUDMapperWithId[PasswordInfoId, PasswordInfoImpl]
    with CRUDMapperWithId[PasswordInfoId, PasswordInfoImpl] {

  override def defaultAlias: Alias[PasswordInfoImpl] =
    createAlias("PI")

  def namedValues(entity: PasswordInfoImpl): Seq[(SQLSyntax, Any)] =
    Seq(
      column.id -> AsIsParameterBinder(idToRawValue(entity.id)),
      column.loginInfoId -> AsIsParameterBinder(entity.loginInfoId.value),
      column.hasher -> entity.hasher,
      column.password -> entity.password,
      column.salt -> entity.salt
    )

  override def extract(
      rs: WrappedResultSet,
      n: scalikejdbc.ResultName[PasswordInfoImpl]): PasswordInfoImpl =
    PasswordInfoImpl(
      id = rs.get(n.id),
      loginInfoId = rs.get(n.loginInfoId),
      hasher = rs.get(n.hasher),
      password = rs.get(n.password),
      salt = rs.get(n.salt)
    )

  override def findByLoginInfoId(id: LoginInfoId)(
      implicit session: DBSession): Option[PasswordInfoImpl] =
    findBy(sqls.eq(column.loginInfoId, id.value))

  override def deleteByLoginInfoId(id: LoginInfoId)(
      implicit session: DBSession): Int =
    deleteBy(sqls.eq(column.loginInfoId, id.value))

}
