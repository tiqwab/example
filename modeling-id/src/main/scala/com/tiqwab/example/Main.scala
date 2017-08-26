package com.tiqwab.example

import com.tiqwab.example.app.repository.ApplicationRepositoryOnJdbc
import com.tiqwab.example.app.{Application, ApplicationId}
import scalikejdbc._
import scalikejdbc.config.DBs

object Main {

  def main(args: Array[String]): Unit = {
    DBs.setupAll()

    val application = Application(
      ApplicationId(1),
      "App1"
    )

    val appRepo = new ApplicationRepositoryOnJdbc("APPLICATIONS")

    DB localTx { implicit session =>
      appRepo.store(application)
      println(appRepo.findById(application.id))
      appRepo.deleteById(application.id)
      println(appRepo.count())
    }
  }

}
