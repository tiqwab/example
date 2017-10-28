package com.tiqwab.replication.play.json

case class JsPath(name: String, parent: Option[JsPath]) { self =>

  def \(key: String): JsPath = JsPath(key, Some(self))

  def write[T: Writes]: Writes[T] = Writes { x =>
    def loop(jsPathOpt: Option[JsPath], elem: JsValue): JsValue = jsPathOpt match {
      case None =>
        elem
      case Some(jsPath) =>
        loop(jsPath.parent, Json.obj(jsPath.name -> elem))
    }
    loop(Some(self), implicitly[Writes[T]].writes(x))
  }

}

object JsPath {

  def \(key: String): JsPath = JsPath(key, None)

}
