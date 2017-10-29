package com.tiqwab.replication.play.json

sealed trait JsValue { self =>

  def \(name: String): JsLookupResult = self match {
    case json: JsObject =>
      json.value.find(_._1 == name) match {
        case Some((_, v)) =>
          JsDefined(v)
        case None =>
          JsUndefined(s"no such element: $name")
      }
    case _ =>
      JsUndefined(s"no such element: $name")
  }

  def as[T](implicit reads: Reads[T]): T =
    reads.reads(self) match {
      case JsSuccess(v) =>
        v
      case JsError(msg) =>
        throw new IllegalArgumentException(msg.toString)
    }

  def asOpt[T](implicit reads: Reads[T]): Option[T] =
    reads.reads(self) match {
      case JsSuccess(v) =>
        Some(v)
      case JsError(_) =>
        None
    }

}

case class JsString(value: String) extends JsValue {
  override def toString: String = "\"" + value.toString + "\""
}
case class JsNumber(value: Double) extends JsValue {
  override def toString: String = value.toString
}
case class JsBoolean(value: Boolean) extends JsValue {
  override def toString: String = value.toString
}
case class JsObject(value: (String, JsValue)*) extends JsValue {
  override def toString: String =
    "{" + value.map { case (k, v) => "\"" + k.toString + "\"" + ":" + v.toString }.mkString(", ") + "}"
}
case class JsArray(value: JsValue*) extends JsValue {
  override def toString: String = "[" + value.map(_.toString).mkString(", ") + "]"
}
case object JsNull extends JsValue {
  override def toString: String = "null"
}

object JsValue {

  implicit def stringToJsString(value: String): JsString = JsString(value)
  implicit def doubleToJsNumber(value: Double): JsNumber = JsNumber(value)
  implicit def booleanToJsBoolean(value: Boolean): JsBoolean = JsBoolean(value)
  // FIXME: Seq(1, 2) -> JsArray(JsNumber(1), JsNumber(2)) のような変換はこれではできない　
  implicit def arrayToJsArray(value: Seq[JsValue]): JsArray = JsArray(value: _*)

}
