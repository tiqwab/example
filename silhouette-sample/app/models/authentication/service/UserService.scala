package models.authentication.service

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import models.ContextCreator
import models.authentication.{
  LoginInfoId,
  User,
  UserId,
  LoginInfo => ALoginInfo
}
import models.authentication.lifecycle.{LoginInfoRepository, UserRepository}
import org.joda.time.DateTime
import play.api.libs.concurrent.Execution.Implicits._
import scalikejdbc.{AutoSession, DBSession}

import scala.concurrent.Future

class UserService(
    userRepository: UserRepository[DBSession],
    loginInfoRepository: LoginInfoRepository[DBSession],
    contextCreator: ContextCreator[DBSession]
) extends IdentityService[User] {

  def create(entity: User)(
      implicit session: DBSession = AutoSession): Future[Unit] =
    contextCreator.futureContext { implicit session =>
      Future {
        userRepository.store(entity)
      }
    }

  def save(loginInfo: LoginInfo, user: User): Future[User] =
    contextCreator.futureContext { implicit session =>
      Future {
        val info = loginInfoRepository
          .findBySilhouetteLoginInfo(loginInfo)
          .map { info =>
            info.withUserId(Some(user.id))
          }
          .getOrElse {
            val id = LoginInfoId(DateTime.now.toInstant.getMillis)
            ALoginInfo(id,
                       loginInfo.providerID,
                       loginInfo.providerKey,
                       Some(user.id))
          }

        userRepository.store(user)
        loginInfoRepository.store(info)
        user
      }
    }

  def findById(id: UserId)(
      implicit session: DBSession = AutoSession): Future[Option[User]] =
    contextCreator.futureContext { implicit session =>
      Future {
        userRepository.findById(id)
      }
    }

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] =
    contextCreator.futureContext { implicit session =>
      Future {
        for {
          info <- loginInfoRepository.findBySilhouetteLoginInfo(loginInfo)
          userId <- info.userId
          user <- userRepository.findById(userId)
        } yield user
      }
    }

}
