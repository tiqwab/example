package models

trait Repository[ID, E <: Entity[ID], Context] {
  def findById(id: ID)(implicit context: Context): Option[E]
  def store(entity: E)(implicit context: Context): Unit
  def deleteById(id: ID)(implicit context: Context): Int
}
