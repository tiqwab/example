package models

trait Entity[ID] {
  def id: ID

  override def equals(obj: scala.Any): Boolean = obj match {
    case o: Entity[ID] => id == o.id
    case _             => false
  }

  override def hashCode(): Int = 31 * id.##
}
