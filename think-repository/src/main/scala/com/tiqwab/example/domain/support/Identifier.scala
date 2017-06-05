package com.tiqwab.example.domain.support

trait Identifier[+A] {

  def value: A

  override def equals(obj: Any): Boolean = obj match {
    case that: Identifier[_] =>
      value == that.value
    case _ =>
      false
  }

  // TODO: Use `##`
  override def hashCode = 31 * value.##

}
