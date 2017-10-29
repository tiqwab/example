package com.tiqwab.replication.play.json

object Json {

  def stringify(json: JsValue): String = json.toString
  def obj(value: (String, JsValue)*): JsObject = JsObject(value: _*)
  def arr(value: JsValue*): JsArray = JsArray(value: _*)

  def toJson[T](obj: T)(implicit writes: Writes[T]): JsValue =
    implicitly[Writes[T]].writes(obj)

  def fromJson[T](json: JsValue)(implicit reads: Reads[T]): T =
    implicitly[Reads[T]].reads(json).get

}
