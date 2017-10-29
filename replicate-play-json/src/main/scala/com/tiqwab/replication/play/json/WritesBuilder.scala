package com.tiqwab.replication.play.json

object WritesBuilder {
  case class CanBuild2[A, B](w1: Writes[A], w2: Writes[B]) {
    def apply[T](f: T => (A, B)): Writes[T] = Writes { t =>
      val (a, b) = f(t)
      // FIXME: JsObject であることを保証できない　
      // 恐らくそれゆえに OWrites が存在する
      w1.writes(a).asInstanceOf[JsObject] ++ w2.writes(b).asInstanceOf[JsObject]
    }
    def and[C](w3: Writes[C]): CanBuild3[A, B, C] = CanBuild3(w1, w2, w3)
  }

  case class CanBuild3[A, B, C](w1: Writes[A], w2: Writes[B], w3: Writes[C]) {
    def apply[T](f: T => (A, B, C)): Writes[T] = Writes { t =>
      val (a, b, c) = f(t)
      // FIXME: JsObject であることを保証できない　
      // 恐らくそれゆえに OWrites が存在する
      w1.writes(a).asInstanceOf[JsObject] ++ w2.writes(b).asInstanceOf[JsObject] ++ w3.writes(c).asInstanceOf[JsObject]
    }
  }
}
