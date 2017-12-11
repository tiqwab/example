package com.tiqwab.replicate.retry

import org.scalatest.FunSuite

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

class PauseTest extends FunSuite {

  test("retry without interval") {
    var count = 3
    val pause = Pause(3, 5.seconds)
    val f = () =>
      Future {
        count = count - 1
        if (count <= 0) {
          "OK"
        } else {
          throw new RuntimeException("fail")
        }
    }

    val result = Await.result(pause(f), Duration.Inf)
    assert(result === "OK")
  }

}
