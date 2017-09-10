package models.authentication

import models.{IsoIdCompanion, StringIdLike}

case class RegistrationTokenId(value: String) extends StringIdLike

object RegistrationTokenId extends IsoIdCompanion[RegistrationTokenId]
