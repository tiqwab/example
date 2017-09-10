package models.authentication.lifecycle

import com.mohiva.play.silhouette.api
import models.Iso
import models.authentication.{LoginInfo, LoginInfoId}
import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapperWithId}

class LoginInfoRepositoryOnJdbc(override val tableName: String)(
    implicit val iso: Iso[Long, LoginInfoId]
) extends LoginInfoRepository[DBSession]
    with SkinnyCRUDMapperWithId[LoginInfoId, LoginInfo]
    with CRUDMapperWithId[LoginInfoId, LoginInfo] {

  override def defaultAlias: Alias[LoginInfo] =
    createAlias("LI")

  def namedValues(entity: LoginInfo): Seq[(SQLSyntax, Any)] = Seq(
    column.id -> AsIsParameterBinder(idToRawValue(entity.id)),
    column.providerId -> entity.providerId,
    column.providerKey -> entity.providerKey,
    column.userId -> entity.userId.map(_.value)
  )

  override def extract(rs: WrappedResultSet,
                       n: scalikejdbc.ResultName[LoginInfo]): LoginInfo =
    LoginInfo(
      id = rs.get(n.id),
      providerId = rs.get(n.providerId),
      providerKey = rs.get(n.providerKey),
      userId = rs.get(n.userId)
    )

  override def findBySilhouetteLoginInfo(
      loginInfo: api.LoginInfo): Option[LoginInfo] = {
    findBy(
      sqls
        .eq(column.providerId, loginInfo.providerID)
        .and(sqls.eq(column.providerKey, loginInfo.providerKey)))
  }

}
