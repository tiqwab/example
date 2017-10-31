package com.tiqwab.replication.play.json

object Json {

  def stringify(json: JsValue): String = json.toString
  def obj(value: (String, JsValue)*): JsObject = JsObject(value: _*)
  def arr(value: JsValue*): JsArray = JsArray(value: _*)

  def toJson[T: Writes](obj: T): JsValue =
    implicitly[Writes[T]].writes(obj)

  def fromJson[T: Reads](json: JsValue): T =
    implicitly[Reads[T]].reads(json).get

}
