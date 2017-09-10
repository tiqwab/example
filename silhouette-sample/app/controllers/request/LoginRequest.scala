package controllers.request

import com.mohiva.play.silhouette.api.util.Credentials
import models.authentication.Email
import play.api.libs.json.{Format, Json}

case class LoginRequest(email: Email, password: String) {

  def credentials: Credentials = Credentials(email.value, password)

}

object LoginRequest {

  implicit val loginRequestJsonFormat: Format[LoginRequest] =
    Json.format[LoginRequest]

}
