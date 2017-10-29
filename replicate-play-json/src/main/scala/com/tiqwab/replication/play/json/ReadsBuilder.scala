package com.tiqwab.replication.play.json

// FIXME: うまくやる方法がわからないので、愚直に引数の数に対して個別に CanBuild を定義
object ReadsBuilder {
  case class CanBuild2[A, B](r1: Reads[A], r2: Reads[B]) {
    def apply[T](f: (A, B) => T) = Reads { json =>
      for {
        a <- r1.reads(json)
        b <- r2.reads(json)
      } yield f(a, b)
    }
    def and[C](r3: Reads[C]): CanBuild3[A, B, C] = CanBuild3(r1, r2, r3)
  }

  case class CanBuild3[A, B, C](r1: Reads[A], r2: Reads[B], r3: Reads[C]) {
    def apply[T](f: (A, B, C) => T) = Reads { json =>
      for {
        a <- r1.reads(json)
        b <- r2.reads(json)
        c <- r3.reads(json)
      } yield f(a, b, c)
    }
  }
}
