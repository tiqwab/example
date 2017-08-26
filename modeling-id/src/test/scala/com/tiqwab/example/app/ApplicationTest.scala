package com.tiqwab.example.app

import org.scalacheck.Prop
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers._

class ApplicationTest extends FunSuite {
  import Gens._

  test("equality check") {
    check(Prop.forAll(applicationGen, applicationGen) { (app1, app2) =>
      println(app1, app2)
      if (app1.id == app2.id) {
        app1 == app2
      } else {
        app1 != app2
      }
    })
  }

}
