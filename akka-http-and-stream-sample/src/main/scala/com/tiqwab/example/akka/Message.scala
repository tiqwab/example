package com.tiqwab.example.akka

import spray.json._

case class Message(
  id: String,
  body: String,
  timestampMillis: Long
)

object Message extends DefaultJsonProtocol {
  implicit val jsonFormat: RootJsonFormat[Message] = jsonFormat3(Message.apply)
}
