package models.authentication

import models.{IsoIdCompanion, LongIdLike}

case class LoginInfoId(value: Long) extends LongIdLike

object LoginInfoId extends IsoIdCompanion[LoginInfoId]
