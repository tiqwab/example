package com.tiqwab.example.json

import play.api.libs.json._

class Name(val value: String)

object Name {
  // JsValue => JsResult
  implicit val reads = Reads[Name] { json =>
    (json \ "name") match {
      case JsDefined(name)     => JsSuccess(new Name(name.as[String]))
      case err @ JsUndefined() => JsError(err.toString)
    }
  }

  implicit val writes = Writes[Name] { name =>
    Json.obj("name" -> name.value)
  }
}
