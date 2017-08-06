package com.tiqwab.example.json.model

import play.api.libs.json.{JsPath}
import play.api.libs.functional.syntax._

case class Person(
    name: String,
    age: Int,
    favorites: Seq[String],
    family: Seq[FamilyMember]
)

object Person {
  // Define by macro
  // implicit val format = Json.format[Person]

  // Define manually
  implicit val format = (
    (JsPath \ "name").format[String] ~
      (JsPath \ "age").format[Int] ~
      (JsPath \ "favorites").format[Seq[String]] ~
      (JsPath \ "family").format[Seq[FamilyMember]]
  )(Person.apply _, unlift(Person.unapply _))
}
