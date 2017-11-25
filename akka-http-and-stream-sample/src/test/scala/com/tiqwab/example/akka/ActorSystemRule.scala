package com.tiqwab.example.akka

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, TestSuite}

import scala.concurrent.Await
import scala.concurrent.duration._

trait ActorSystemRule extends BeforeAndAfterAll { this: TestKit with TestSuite =>

  override def afterAll(): Unit = {
    super.afterAll()
    Await.result(system.terminate(), Duration.Inf)
  }

}
