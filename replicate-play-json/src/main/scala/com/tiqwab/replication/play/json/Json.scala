package com.tiqwab.replication.play.json

object Json {

  def stringify(json: JsValue): String = json.toString
  def obj(value: (String, JsValue)*): JsObject = JsObject(value: _*)
  def arr(value: JsValue*): JsArray = JsArray(value: _*)

}
