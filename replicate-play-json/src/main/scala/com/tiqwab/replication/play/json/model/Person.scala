package com.tiqwab.replication.play.json.model

import com.tiqwab.replication.play.json.{JsError, JsResult, JsSuccess, Reads}

case class Person(name: String, age: Double)

object Person {
  implicit val reads: Reads[Person] = Reads[Person] { json =>
    val nameOpt = (json \ "name").get.asOpt[String]
    val ageOpt = (json \ "age").get.asOpt[Double]
    (for {
      name <- nameOpt
      age <- ageOpt
    } yield Person(name, age)).fold[JsResult[Person]](JsError(Seq("cannot read as Person")))(JsSuccess(_))
  }
}
