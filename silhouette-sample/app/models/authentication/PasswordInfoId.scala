package models.authentication

import models.{IsoIdCompanion, LongIdLike}

case class PasswordInfoId(value: Long) extends LongIdLike

object PasswordInfoId extends IsoIdCompanion[PasswordInfoId]
