package com.tiqwab.example.json

import play.api.libs.json.{Json}

case class Age(age: Int)

object Age {
  implicit val format = Json.format[Age]
}
