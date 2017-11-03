package com.tiqwab.replication.play.json

// FIXME: どういうデータ構造がいいんだろう
// parnet, child ともに持たせるような双方向の依存にするべき？
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

  def read[T: Reads]: Reads[T] = Reads { json =>
    def listPath(jsPathOpt: Option[JsPath], paths: Seq[JsPath]): Seq[JsPath] = jsPathOpt match {
      case None =>
        paths
      case Some(jsPath) =>
        listPath(jsPath.parent, jsPath +: paths)
    }
    def loop(paths: Seq[JsPath], elem: JsValue): JsResult[T] = paths.headOption match {
      case None =>
        implicitly[Reads[T]].reads(elem)
      case Some(jsPath) =>
        (elem \ jsPath.name).toEither match {
          case Left(msg) =>
            JsError(Seq(msg))
          case Right(j) =>
            loop(paths.tail, j)
        }
      // TODO: fold だと末尾再帰にならない??
      // (elem \ jsPath.name).toEither.fold(msg => JsError(Seq(msg)), j => loop(paths.tail, j))
    }
    loop(listPath(Some(self), Seq.empty[JsPath]), json)
  }

}

object JsPath {

  def \(key: String): JsPath = JsPath(key, None)

}
