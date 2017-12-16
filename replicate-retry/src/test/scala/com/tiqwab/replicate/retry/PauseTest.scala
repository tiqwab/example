package com.tiqwab.replicate.retry

import org.scalatest.FunSuite

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

class PauseTest extends FunSuite {

  test("retry with Pause") {
    var count = 3
    val pause = Pause(3, 2.seconds)
    implicit val success = Success.always
    val f = () =>
      Future {
        count = count - 1
        if (count <= 0) {
          "OK"
        } else {
          throw new RuntimeException("fail")
        }
    }

    val startMillis = System.currentTimeMillis
    val result = Await.result(pause(f), Duration.Inf)
    val endMillis = System.currentTimeMillis
    assert(count === 0)
    assert(result === "OK")
    assert(endMillis - startMillis > 4000)
  }

  test("use Success.option") {
    var count = 3
    val pause = Pause(3, 2.seconds)
    implicit val success = Success.option
    val f = () =>
      Future {
        count = count - 1
        if (count <= 0) {
          Some("OK")
        } else {
          None
        }
    }

    val result = Await.result(pause(f), Duration.Inf)
    assert(result === Some("OK"))
  }

}
