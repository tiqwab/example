package com.tiqwab.example.app

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers._

class ApplicationIdTest extends FunSuite {
  import Gens._
  import PropUtils._

  test("iso check") {
    implicit val longGen = positiveLongGen
    implicit val appIdGen = applicationIdGen
    check(isoLaws[Long, ApplicationId])
  }

}
