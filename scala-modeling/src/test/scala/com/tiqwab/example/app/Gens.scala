package com.tiqwab.example.app

import java.time.ZoneId

import org.scalacheck.Gen
import org.scalacheck.Arbitrary._

trait Gens {

  lazy val applicationIdGen =
    arbitrary[Long].retryUntil(_ > 0).map(ApplicationId(_))

  lazy val zonedDateTimeGen =
    Gen.calendar.map(_.toInstant.atZone(ZoneId.of("UTC")))

  lazy val applicationGen =
    for {
      id <- applicationIdGen
      name <- arbitrary[String]
      createdAt <- zonedDateTimeGen
      updatedAt = createdAt
    } yield {
      Application(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
      )
    }

}

object Gens extends Gens
