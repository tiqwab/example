package com.tiqwab.example.app

trait Food {
  val name: String
}

trait Animal {
  type MyFood <: Food
  def eat(food: MyFood): Unit
}

class DogFood(override val name: String) extends Food

class Dog extends Animal {
  override type MyFood = DogFood
  val x: MyFood = new DogFood("hoge")
  override def eat(food: MyFood): Unit = println(s"Dog eat $food")
}

trait CatFood extends Food {
  override val name: String = "cat food"
}

object CatFood extends CatFood

class Cat extends Animal {
  override type MyFood = CatFood
  val x: MyFood = CatFood
  override def eat(food: CatFood): Unit = println(s"Cat eat $food")
}
