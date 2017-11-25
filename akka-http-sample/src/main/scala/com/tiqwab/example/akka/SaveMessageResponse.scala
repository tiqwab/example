package com.tiqwab.example.akka

import spray.json._

case class SaveMessageResponse(
  id: String
)

object SaveMessageResponse extends DefaultJsonProtocol {
  implicit val jsonFormat: RootJsonFormat[SaveMessageResponse] = jsonFormat1(SaveMessageResponse.apply)
}

/*
sealed trait SaveMessageResponse

object SaveMessageResponse extends DefaultJsonProtocol {

  implicit val jsonFormat: RootJsonFormat[SaveMessageResponse] = new RootJsonFormat[SaveMessageResponse] {
    override def read(json: JsValue): SaveMessageResponse =
      json.asJsObject.getFields("status") match {
        case Seq(JsNumber(Success.status)) =>
          Success.jsonFormat.read(json)
        case Seq(JsNumber(ServerError.status)) =>
          ServerError.jsonFormat.read(json)
      }
    override def write(obj: SaveMessageResponse): JsValue = ???
  }

  case class Success(id: String) extends SaveMessageResponse

  object Success {
    val status = BigDecimal(202)
    implicit val jsonFormat: RootJsonFormat[Success] = jsonFormat1(Success.apply)
  }

  case class ServerError(errorMessage: String) extends SaveMessageResponse

  object ServerError {
    val status = BigDecimal(500)
    implicit val jsonFormat: RootJsonFormat[ServerError] = jsonFormat1(ServerError.apply)
  }
}
 */
