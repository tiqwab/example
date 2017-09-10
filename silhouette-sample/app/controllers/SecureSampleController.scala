package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.exceptions.{
  IdentityNotFoundException,
  InvalidPasswordException
}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.request.{CreateUserRequest, LoginRequest}
import models.ContextCreator
import models.authentication._
import models.authentication.service.{LoginService, UserService}
import org.joda.time.DateTime
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.concurrent.Execution.Implicits._
import scalikejdbc.DBSession

import scala.concurrent.Future

class SecureSampleController @Inject()(
    userService: UserService,
    contextCreator: ContextCreator[DBSession],
    loginService: LoginService,
    credentialsProvider: CredentialsProvider,
    silhouette: Silhouette[TokenEnv]
) extends Controller {

  def signup(): Action[CreateUserRequest] =
    Action.async(jsonParser[CreateUserRequest]) { implicit request =>
      val now = DateTime.now
      contextCreator.futureContext { implicit session =>
        loginService
          .signup(request.body.email, request.body.password, now)
          .map {
            case Left(msg) =>
              BadRequest(msg)
            case Right(token) =>
              Ok(s"registrationToken: ${token.id}")
          }
      }
    }

  def confirmSignup(
      registrationTokenId: RegistrationTokenId): Action[AnyContent] =
    Action.async { implicit request =>
      val now = DateTime.now
      contextCreator.futureContext { implicit session =>
        loginService
          .confirmSignup(registrationTokenId, now, _ => Ok("confirmed signup"))
          .map {
            case Left(msg) =>
              BadRequest(msg)
            case Right(result) =>
              result
          }
      }
    }

  def login(): Action[LoginRequest] =
    Action.async(jsonParser[LoginRequest]) { implicit loginRequest =>
      lazy val failureResponse = Future.successful(BadRequest("login failed"))

      credentialsProvider
        .authenticate(loginRequest.body.credentials)
        .flatMap { loginInfo =>
          userService.retrieve(loginInfo).flatMap {
            case Some(user) =>
              silhouette.env.authenticatorService.create(loginInfo).flatMap {
                authenticator =>
                  silhouette.env.authenticatorService
                    .init(authenticator)
                    .flatMap { value =>
                      silhouette.env.authenticatorService
                        .embed(value, Ok("completed login"))
                    }
              }
            case None =>
              failureResponse
          }
        }
        .recoverWith {
          case _: IdentityNotFoundException => failureResponse
          case _: InvalidPasswordException  => failureResponse
        }
    }

  def logout(): Action[AnyContent] =
    silhouette.UserAwareAction.async { implicit request =>
      request.authenticator match {
        case Some(authenticator) =>
          silhouette.env.authenticatorService
            .discard(authenticator, Ok("completed logout"))
        case None =>
          Future.successful(Ok("not login now"))
      }
    }

  def securedContent: Action[AnyContent] =
    silhouette.SecuredAction.async { implicit request =>
      Future.successful(Ok("secured content!"))
    }

}
