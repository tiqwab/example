package models.authentication.lifecycle

import models.Repository
import models.authentication.{LoginInfoId, PasswordInfoId, PasswordInfoImpl}
import scalikejdbc.DBSession

trait PasswordInfoRepository[Context]
    extends Repository[PasswordInfoId, PasswordInfoImpl, Context] {

  def findByLoginInfoId(id: LoginInfoId)(
      implicit session: DBSession): Option[PasswordInfoImpl]

  def deleteByLoginInfoId(id: LoginInfoId)(implicit session: DBSession): Int

}
