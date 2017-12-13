package com.tiqwab.replicate.retry

trait Success[-T] {

  def predicate(v: T): Boolean
  def and[TT <: T](success: Success[TT]): Success[TT] =
    (x: TT) => predicate(x) && success.predicate(x)
  def or[TT <: T](success: Success[TT]): Success[TT] =
    (x: TT) => predicate(x) || success.predicate(x)

}

object Success {

  val always: Success[Any] = (_: Any) => true
  val option: Success[Option[_]] = (x: Option[_]) => x.isDefined

}
