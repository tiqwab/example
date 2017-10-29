package com.tiqwab.replication.play.json

trait Reads[T] {

  def reads(json: JsValue): JsResult[T]

}

object Reads {

  def apply[T](f: JsValue => JsResult[T]): Reads[T] = new Reads[T] {
    override def reads(json: JsValue): JsResult[T] = f(json)
  }

  implicit def stringReads: Reads[String] = Reads {
    case JsString(v) =>
      JsSuccess(v)
    case x =>
      JsError(Seq(s"$x cannot be read to String"))
  }
  implicit def doubleReads: Reads[Double] = Reads {
    case JsNumber(v) =>
      JsSuccess(v)
    case x =>
      JsError(Seq(s"$x cannot be read to Double"))
  }
  implicit def booleanReads: Reads[Boolean] = Reads {
    case JsBoolean(v) =>
      JsSuccess(v)
    case x =>
      JsError(Seq(s"$x cannot be read to Boolean"))
  }
  implicit def seqReads[T: Reads]: Reads[Seq[T]] = Reads {
    case arr: JsArray =>
      JsResult.sequence(arr.value.map(implicitly[Reads[T]].reads(_)))
    case x =>
      JsError[Seq[T]](Seq(s"$x cannot be read to Seq"))
  }

}
