package com.tiqwab.example.scalacheck

case class Person(id: Long, name: String, age: Int) {

  /*
  override def hashCode(): Int = 31 * id.##
  override def equals(obj: scala.Any): Boolean = {
    if (obj == null) {
      return false
    }
    if (obj.getClass != classOf[Person]) {
      return false
    }
    val p = obj.asInstanceOf[Person]
    id == p.id
  }
 */
}
