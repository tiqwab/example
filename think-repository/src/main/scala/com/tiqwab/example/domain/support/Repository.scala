package com.tiqwab.example.domain.support

import scala.util.Try

trait Repository[ID <: Identifier[_], E <: Entity[ID]] {

  type Ctx = EntityIOContext

  def save(entity: E)(implicit ct: Ctx): E
  def findById(id: ID)(implicit ctx: Ctx): Try[E]

}
