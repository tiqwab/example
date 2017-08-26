package com.tiqwab.example.modeling

trait Iso[A, B] {
  def get(a: A): B
  def reverseGet(b: B): A
}

object Iso {
  def apply[A, B](_get: (A) => B)(_reverseGet: (B) => A): Iso[A, B] =
    new Iso[A, B] {
      override def get(a: A): B = _get(a)
      override def reverseGet(b: B): A = _reverseGet(b)
    }
}
