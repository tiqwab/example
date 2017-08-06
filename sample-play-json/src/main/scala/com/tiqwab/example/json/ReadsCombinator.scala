package com.tiqwab.example.json

import play.api.libs.json._
import play.api.libs.functional.syntax._

object ReadsCombinator extends App {
  // To show types
  // val namePath: JsPath = JsPath \ 'name
  // val nameReads: Reads[String] = namePath.read[String]
  // val builderReads: FunctionalBuilder[Reads]#CanBuild2[String, Int] =
  //   (JsPath \ "name").read[String] ~
  //     (JsPath \ "age").read[Int]

  implicit val personReads: Reads[Person] = (
    (JsPath \ "name").read[String] ~
      (JsPath \ "age").read[Int]
  )(Person.apply _)

  val json = Json.obj("name" -> "Alice", "age" -> 20)
  json.validate[Person] match {
    case s: JsSuccess[Person] => println(s.get) // Person(Alice, 20)
    case e: JsError           => println(e)
  }
}
