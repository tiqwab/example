package models.authentication

import com.mohiva.play.silhouette.api.Identity
import models.Entity
import org.joda.time.DateTime

final case class User(
    id: UserId,
    email: Email,
    createdAt: DateTime
) extends Entity[UserId]
    with Identity
