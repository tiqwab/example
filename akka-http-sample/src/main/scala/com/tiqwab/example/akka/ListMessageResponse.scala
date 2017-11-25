package com.tiqwab.example.akka

import spray.json._

case class ListMessageResponse(messages: Seq[Message])

object ListMessageResponse extends DefaultJsonProtocol {
  implicit val jsonFormat: RootJsonFormat[ListMessageResponse] = jsonFormat1(ListMessageResponse.apply)
}
