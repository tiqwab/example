package com.tiqwab.example.akka

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.concurrent.Await
import scala.concurrent.duration._

trait ActorSystemRule extends BeforeAndAfterAll { this: TestKit with Suite =>

  override def afterAll(): Unit = {
    super.afterAll()
    Await.result(system.terminate(), Duration.Inf)
  }

}
