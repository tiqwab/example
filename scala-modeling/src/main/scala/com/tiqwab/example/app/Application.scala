package com.tiqwab.example.app

import java.time.ZonedDateTime

import com.tiqwab.example.modeling.{Entity, IsoLongIdCompanion, LongIdLike}

case class ApplicationId(value: Long) extends LongIdLike

object ApplicationId extends IsoLongIdCompanion[ApplicationId]

case class Application(
    id: ApplicationId,
    name: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
) extends Entity[ApplicationId]
