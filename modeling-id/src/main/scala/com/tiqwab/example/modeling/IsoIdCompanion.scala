package com.tiqwab.example.modeling

trait IsoIdCompanion[A <: IdLike] {
  def apply(a: A#Value): A

  implicit lazy val iso: Iso[A#Value, A] =
    Iso[A#Value, A](apply)(_.value)
}
