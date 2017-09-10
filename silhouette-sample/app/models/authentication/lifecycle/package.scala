package models.authentication

import models.Iso
import scalikejdbc.TypeBinder

package object lifecycle {

  def isoTypeBinder[A: TypeBinder, B](implicit iso: Iso[A, B]): TypeBinder[B] =
    implicitly[TypeBinder[A]].map(iso.get(_))

  implicit val userIdTypeBinder = isoTypeBinder[Long, UserId]

  implicit val loginInfoIdTypeBinder = isoTypeBinder[Long, LoginInfoId]

  implicit val passwordInfoIdTypeBinder = isoTypeBinder[Long, PasswordInfoId]

  implicit val registrationTokenIdTypeBinder =
    isoTypeBinder[String, RegistrationTokenId]

}
