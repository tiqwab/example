package com.tiqwab.example.app

import com.tiqwab.example.modeling.{IsoIdCompanion, LongIdLike}

case class ApplicationId(value: Long) extends LongIdLike

object ApplicationId extends IsoIdCompanion[ApplicationId]
