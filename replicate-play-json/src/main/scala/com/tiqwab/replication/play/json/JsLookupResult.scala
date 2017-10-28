package com.tiqwab.replication.play.json

sealed trait JsLookupResult { self =>

  def get: JsValue
  def toOption: Option[JsValue]
  def toEither: Either[String, JsValue]

  def \(name: String): JsLookupResult = self match {
    case JsDefined(json) =>
      json \ name
    case x @ JsUndefined(_) =>
      x
  }

}

case class JsDefined(value: JsValue) extends JsLookupResult {
  override def get: JsValue = value
  override def toOption: Option[JsValue] = Some(value)
  override def toEither: Either[String, JsValue] = Right(value)
}
case class JsUndefined(message: String) extends JsLookupResult {
  override def get: JsValue = throw new NoSuchElementException(message)
  override def toOption: Option[JsValue] = None
  override def toEither: Either[String, JsValue] = Left(message)
}
