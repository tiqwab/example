package models.authentication

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Email(value: String)

object Email {
  implicit val emailFormat: Format[Email] =
    implicitly[Format[String]].inmap(Email.apply, _.value)
}
