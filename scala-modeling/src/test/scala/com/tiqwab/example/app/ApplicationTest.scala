package com.tiqwab.example.app

import org.scalacheck.Prop
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers._

class ApplicationTest extends FunSuite {
  import Gens._

  test("check equality of Application") {
    check(Prop.forAll(applicationGen, applicationGen) {
      (x: Application, y: Application) =>
        if (x.id === y.id) x === y
        else x !== y
    })
  }

  test("check hashCode of Application") {
    check(Prop.forAll(applicationGen, applicationGen) {
      (x: Application, y: Application) =>
        if (x.id === y.id) x.hashCode === y.hashCode
        else true
    })
  }

}
