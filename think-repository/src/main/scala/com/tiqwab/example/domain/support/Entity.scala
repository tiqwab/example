package com.tiqwab.example.domain.support

trait Entity[ID <: Identifier[_]] {

  // TODO: Just override This to implement canEqual correctly
  type This <: Entity[ID]

  def id: ID

  // TODO: Why @unchecked is necesssary?
  def canEqual(other: Any): Boolean = other.isInstanceOf[This @unchecked]

  // TODO: Implement equals method (ref. chap.30 in Programming in Scala)
  override def equals(obj: Any): Boolean = obj match {
    case that: Entity[_] =>
      that.canEqual(this) && id == that.id
    case _ =>
      false
  }

  override def hashCode = 31 * id.##

}
