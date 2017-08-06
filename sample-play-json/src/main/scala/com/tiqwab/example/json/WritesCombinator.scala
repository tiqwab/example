package com.tiqwab.example.json

import play.api.libs.json._
import play.api.libs.functional.syntax._

object WritesCombinator extends App {
  // def play.api.libs.functional.syntax.unlift[A, B](f: A => Option[B]): (A) => B
  implicit val personReads: Writes[Person] = (
    (JsPath \ "name").write[String] ~
      (JsPath \ "age").write[Int]
  )(unlift(Person.unapply))

  val json = Json.obj("name" -> "Alice", "age" -> 20)
  val person = Person("Alice", 20)
  println(Json.toJson(person)) // {"name":"Alice","age":20}
}
