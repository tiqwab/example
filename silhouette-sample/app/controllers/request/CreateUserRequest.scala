package controllers.request

import models.authentication.Email
import play.api.libs.json.Json

case class CreateUserRequest(
    email: Email,
    password: String
)

object CreateUserRequest {
  implicit val createUserRequestFormat = Json.format[CreateUserRequest]
}
