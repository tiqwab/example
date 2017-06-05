package com.tiqwab.example.domain.support

import scala.util.Try

trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  def save(entity: E): E
  def findById(id: ID): Try[E]

}
