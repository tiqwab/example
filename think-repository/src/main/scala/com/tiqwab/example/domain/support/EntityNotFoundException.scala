package com.tiqwab.example.domain.support

case class EntityNotFoundException(message: String) extends Exception(message){

}

object EntityNotFoundException {

  def apply(identifier: Identifier[Any]): EntityNotFoundException =
    EntityNotFoundException(s"Entity is not found(identifier = $identifier)")

}
