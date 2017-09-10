package models.authentication

import models.{IsoIdCompanion, LongIdLike}

case class UserId(value: Long) extends LongIdLike

object UserId extends IsoIdCompanion[UserId]
