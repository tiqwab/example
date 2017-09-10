package models.authentication.service

import javax.inject.Inject

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{Silhouette, LoginInfo => SLoginInfo}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.ContextCreator
import models.authentication.lifecycle.RegistrationTokenRepository
import models.authentication._
import org.joda.time.DateTime
import scalikejdbc.{AutoSession, DBSession}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Random

// FIXME: Controller 側に依存してしまうので、あまり Service にわける嬉しみがない
class LoginService @Inject()(
    contextCreator: ContextCreator[DBSession],
    userService: UserService,
    passwordHasher: PasswordHasher,
    registrationTokenRepo: RegistrationTokenRepository[DBSession],
    authInfoRepo: AuthInfoRepository,
    silhouette: Silhouette[TokenEnv]
) {

  val expirationTime = 30.minutes

  def signup(email: Email, rawPassword: String, createdAt: DateTime)(
      implicit session: DBSession = AutoSession)
    : Future[Either[String, RegistrationToken]] = {
    val sLoginInfo = SLoginInfo(CredentialsProvider.ID, email.value)
    userService
      .retrieve(sLoginInfo)
      .flatMap {
        case Some(user) =>
          Future.successful(Left(s"user already exists: ${email.value}"))
        case None =>
          // TODO: Should use salt?
          val passwordInfo = passwordHasher.hash(rawPassword)
          val token: RegistrationToken = RegistrationToken(
            id = RegistrationTokenId(Random.alphanumeric.take(32).mkString),
            email = email,
            hasher = passwordInfo.hasher,
            password = passwordInfo.password,
            salt = passwordInfo.salt,
            createdAt = createdAt,
            expiresAt = createdAt.plusMinutes(expirationTime.toMinutes.toInt)
          )
          registrationTokenRepo.store(token)
          Future.successful(Right(token))
      }
  }

  def confirmSignup(tokenId: RegistrationTokenId,
                    createdAt: DateTime,
                    f: (User) => Result)(
      implicit session: DBSession = AutoSession,
      requestHeader: RequestHeader): Future[Either[String, Result]] =
    Future(registrationTokenRepo.findById(tokenId))
      .flatMap {
        case None =>
          Future.successful(Left(s"token not found: ${tokenId.value}"))
        case Some(token) =>
          val sLoginInfo = SLoginInfo(CredentialsProvider.ID, token.email.value)
          userService
            .retrieve(sLoginInfo)
            .flatMap {
              case Some(user) =>
                Future.successful(
                  Left(s"user already exists: ${user.email.value}"))
              case None =>
                val user = User(
                  // TODO: Assign id
                  id = UserId(DateTime.now.toInstant.getMillis),
                  email = token.email,
                  createdAt = createdAt
                )
                for {
                  _ <- userService.save(sLoginInfo, user)
                  authInfo <- authInfoRepo.add(sLoginInfo,
                                               token.silhouettePasswordInfo)
                  _ <- Future(registrationTokenRepo.deleteById(tokenId))
                  authenticator <- silhouette.env.authenticatorService
                    .create(sLoginInfo)
                  value <- silhouette.env.authenticatorService
                    .init(authenticator)
                  result <- silhouette.env.authenticatorService
                    .embed(value, f(user))
                } yield Right(result)
            }
      }

}
