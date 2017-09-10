package models.authentication

import models.Entity
import org.joda.time.DateTime
import com.mohiva.play.silhouette.api.util.{PasswordInfo => SPasswordInfo}

case class RegistrationToken(
    id: RegistrationTokenId,
    email: Email,
    hasher: String,
    password: String,
    salt: Option[String],
    createdAt: DateTime,
    expiresAt: DateTime
) extends Entity[RegistrationTokenId] {

  def silhouettePasswordInfo: SPasswordInfo =
    SPasswordInfo(
      hasher,
      password,
      salt
    )
}
