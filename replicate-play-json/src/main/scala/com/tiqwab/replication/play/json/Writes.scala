package com.tiqwab.replication.play.json

trait Writes[T] {

  def writes(value: T): JsValue

}

object Writes {

  def apply[T](f: T => JsValue) = new Writes[T] {
    override def writes(value: T): JsValue = f(value)
  }

  implicit def stringWrites: Writes[String] = Writes { x =>
    JsString(x)
  }
  implicit def doubleWrites: Writes[Double] = Writes { x =>
    JsNumber(x)
  }
  implicit def booleanWrites: Writes[Boolean] = Writes { x =>
    JsBoolean(x)
  }
  implicit def seqWrites[T: Writes]: Writes[Seq[T]] = Writes { xs =>
    xs.map(implicitly[Writes[T]].writes(_))
  }

}
