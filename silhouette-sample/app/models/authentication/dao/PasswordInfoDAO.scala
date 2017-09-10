package models.authentication.dao

import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import models.ContextCreator
import models.authentication.PasswordInfoId
import models.authentication.lifecycle.{
  LoginInfoRepository,
  PasswordInfoRepository
}
import org.joda.time.DateTime
import scalikejdbc.DBSession

import scala.concurrent.{ExecutionContext, Future}

class PasswordInfoDAO @Inject()(
    loginInfoRepository: LoginInfoRepository[DBSession],
    passwordInfoRepository: PasswordInfoRepository[DBSession],
    contextCreator: ContextCreator[DBSession]
)(
    implicit ec: ExecutionContext
) extends DelegableAuthInfoDAO[PasswordInfo] {

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] =
    contextCreator.futureContext { implicit session =>
      Future {
        for {
          info <- loginInfoRepository.findBySilhouetteLoginInfo(loginInfo)
          pInfo <- passwordInfoRepository.findByLoginInfoId(info.id)
        } yield pInfo.silhouettePasswordInfo
      }
    }

  override def add(loginInfo: LoginInfo,
                   authInfo: PasswordInfo): Future[PasswordInfo] =
    save(loginInfo, authInfo)

  override def update(loginInfo: LoginInfo,
                      authInfo: PasswordInfo): Future[PasswordInfo] =
    save(loginInfo, authInfo)

  override def save(loginInfo: LoginInfo,
                    authInfo: PasswordInfo): Future[PasswordInfo] =
    contextCreator.futureContext { implicit session =>
      Future {
        val info = loginInfoRepository
          .findBySilhouetteLoginInfo(loginInfo)
          .getOrElse(sys.error(s"LoginInfo $loginInfo not found"))
        val pInfo = models.authentication.PasswordInfoImpl(
          PasswordInfoId(DateTime.now.toInstant.getMillis),
          info.id,
          authInfo.hasher,
          authInfo.password,
          authInfo.salt
        )
        passwordInfoRepository.store(pInfo)
        authInfo
      }
    }

  override def remove(loginInfo: LoginInfo): Future[Unit] =
    contextCreator.futureContext { implicit session =>
      Future {
        for {
          info <- loginInfoRepository.findBySilhouetteLoginInfo(loginInfo)
        } yield {
          passwordInfoRepository.deleteByLoginInfoId(info.id)
          loginInfoRepository.deleteById(info.id)
        }
      }
    }

}
