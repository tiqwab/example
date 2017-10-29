package com.tiqwab.replication.play.json

sealed trait JsResult[T] { self =>
  def get: T
  def map[U](f: T => U): JsResult[U] = self match {
    case JsSuccess(v) =>
      JsSuccess(f(v))
    case JsError(msgs) =>
      JsError(msgs)
  }
  def flatMap[U](f: T => JsResult[U]): JsResult[U] = self match {
    case JsSuccess(v) =>
      f(v)
    case JsError(msgs) =>
      JsError(msgs)
  }
}

case class JsSuccess[T](value: T) extends JsResult[T] {
  override def get: T = value
}
case class JsError[T](messages: Seq[String]) extends JsResult[T] {
  override def get: T = throw new NoSuchElementException(s"no such element: $messages")
}

object JsResult {

  def sequence[T](results: Seq[JsResult[T]]): JsResult[Seq[T]] = {
    def loop(res: Seq[JsResult[T]], result: JsResult[Seq[T]]): JsResult[Seq[T]] =
      res.headOption match {
        case None =>
          result
        case Some(x) =>
          val nextResult = x match {
            case JsSuccess(v) =>
              result match {
                case JsSuccess(vs) =>
                  JsSuccess(v +: vs)
                case JsError(msgs) =>
                  JsError[Seq[T]](msgs)
              }
            case JsError(msg) =>
              result match {
                case _: JsSuccess[Seq[T]] =>
                  JsError[Seq[T]](msg)
                case JsError(msgs) =>
                  JsError[Seq[T]](msg ++ msgs)
              }
          }
          loop(res.tail, nextResult)
      }
    loop(results, JsSuccess(Seq.empty[T]))
  }

}
