package com.tiqwab.replication.play.json

trait Writes[T] { self =>

  def writes(value: T): JsValue

  def and[U](another: Writes[U]): WritesBuilder.CanBuild2[T, U] =
    WritesBuilder.CanBuild2(self, another)

}

object Writes {

  def apply[T](f: T => JsValue) = new Writes[T] {
    override def writes(value: T): JsValue = f(value)
  }

  implicit def stringWrites: Writes[String] = Writes { x =>
    JsString(x)
  }
  implicit def intWrites: Writes[Int] = Writes { x =>
    JsNumber(x)
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
