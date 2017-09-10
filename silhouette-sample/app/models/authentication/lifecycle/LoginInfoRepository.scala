package models.authentication.lifecycle

import com.mohiva.play.silhouette.api
import models.Repository
import models.authentication.{LoginInfo, LoginInfoId}

trait LoginInfoRepository[Context]
    extends Repository[LoginInfoId, LoginInfo, Context] {

  def findBySilhouetteLoginInfo(loginInfo: api.LoginInfo): Option[LoginInfo]

}
