package com.tiqwab.example.modeling

trait Repository[ID, E <: Entity[ID], Context] {
  def findById(id: ID)(implicit ctx: Context): Option[E]
  def store(entity: E)(implicit ctx: Context): E
  def deleteById(id: ID)(implicit ctx: Context): Int
}
