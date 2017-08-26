package com.tiqwab.example.modeling

trait Entity[ID] {
  def id: ID

  override def equals(obj: Any): Boolean = obj match {
    case e: Entity[ID] => this.id == e.id
    case _             => false
  }

  override def hashCode(): Int = 31 * id.##
}
