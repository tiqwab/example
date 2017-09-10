package models.authentication

import models.Entity
import com.mohiva.play.silhouette.api.util

trait PasswordInfo extends Entity[PasswordInfoId] {
  def silhouettePasswordInfo: util.PasswordInfo
}
