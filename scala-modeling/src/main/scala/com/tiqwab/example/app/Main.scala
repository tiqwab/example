package com.tiqwab.example.app

import java.time.ZonedDateTime

import scalikejdbc._
import scalikejdbc.config.DBs

object Main {
  def main(args: Array[String]): Unit = {
    /*
    val dogFood = new DogFood("dog food")
    val dog = new Dog()
    dog.eat(dogFood)
    println(dog.x)
    val cat = new Cat()
    cat.eat(CatFood)
    println(cat.x)
     */

    val applicationRepo = new ApplicationRepositoryOnJdbc("APPLICATIONS")

    DBs.setupAll()
    DB localTx { implicit session =>
      val app = Application(
        id = ApplicationId(1),
        name = "App1",
        createdAt = ZonedDateTime.now(),
        updatedAt = ZonedDateTime.now()
      )
      applicationRepo.store(app)
      println(applicationRepo.findById(ApplicationId(1)))
      applicationRepo.deleteById(ApplicationId(1))
      println(applicationRepo.count())
    }
  }
}
