package models.authentication

import com.mohiva.play.silhouette.api
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.Entity

case class LoginInfo(
    id: LoginInfoId,
    providerId: String,
    providerKey: String,
    userId: Option[UserId]
) extends Entity[LoginInfoId] {

  def silhouetteLoginInfo: api.LoginInfo =
    api.LoginInfo(providerId, providerKey)

  def withUserId(userId: Option[UserId]): LoginInfo =
    copy(userId = userId)

  def withEmail(email: Email): LoginInfo = providerId match {
    case CredentialsProvider.ID => copy(providerKey = email.value)
    case _                      => this
  }

}
