package models.authentication
import com.mohiva.play.silhouette.api.util

case class PasswordInfoImpl(
    id: PasswordInfoId,
    loginInfoId: LoginInfoId,
    hasher: String,
    password: String,
    salt: Option[String]
) extends PasswordInfo {

  override def silhouettePasswordInfo: util.PasswordInfo =
    util.PasswordInfo(
      hasher,
      password,
      salt
    )

}
