package com.tiqwab.replicate.retry

import org.scalatest.FunSuite

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

class BackoffTest extends FunSuite {

  test("retry with Backoff") {
    var count = 4
    var timeList = Seq.empty[Long]
    val backoff = Backoff(4, 1.seconds, 2)
    val f = () =>
      Future {
        count = count - 1
        if (count <= 0) {
          "OK"
        } else {
          timeList = System.currentTimeMillis +: timeList
          throw new RuntimeException("fail")
        }
    }

    val startMillis = System.currentTimeMillis
    val result = Await.result(backoff(f), Duration.Inf)
    val endMillis = System.currentTimeMillis
    assert(result === "OK")
    assert(timeList.length === 3)
    assert(timeList(0) - timeList(1) >= 2000)
    assert(timeList(1) - timeList(2) >= 1000)
    assert(endMillis - startMillis > 7000)
  }

}
