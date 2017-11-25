package com.tiqwab.example.akka

import spray.json._

case class SaveMessageRequest(
  body: String,
  timestampMillis: Long
)

object SaveMessageRequest extends DefaultJsonProtocol {
  implicit val jsonFormat: RootJsonFormat[SaveMessageRequest] = jsonFormat2(SaveMessageRequest.apply)
}
