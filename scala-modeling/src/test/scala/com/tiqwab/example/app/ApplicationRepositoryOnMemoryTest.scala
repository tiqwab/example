package com.tiqwab.example.app

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers._
import org.scalacheck.Prop

class ApplicationRepositoryOnMemoryTest extends FunSuite {
  import Gens._

  test("findById returns entity if exist") {
    val repo = new ApplicationRepositoryOnMemory()
    check(Prop.forAll(applicationGen) { app =>
      repo.store(app)
      val appOpt = repo.findById(app.id)
      appOpt.get === app
    })
  }

  test("findById returns None if not exist") {
    val repo = new ApplicationRepositoryOnMemory()
    check(Prop.forAll(applicationIdGen) { id =>
      repo.findById(id).isEmpty
    })
  }

  test("store entity") {
    val repo = new ApplicationRepositoryOnMemory()
    check(Prop.forAll(applicationGen) { app =>
      val count = repo.count()
      if (repo.findById(app.id).isEmpty) {
        repo.store(app)
        repo.count() === count + 1
      } else {
        repo.store(app)
        repo.count() === count
      }
    })
  }

  test("deleteById returns 1 if exist") {
    val repo = new ApplicationRepositoryOnMemory()
    check(Prop.forAll(applicationGen) { app =>
      repo.store(app)
      repo.deleteById(app.id) === 1
      repo.count() === 0
    })
  }

  test("deleteById returns 0 if not exist") {
    val repo = new ApplicationRepositoryOnMemory()
    check(Prop.forAll(applicationIdGen) { id =>
      repo.deleteById(id) === 0
    })
  }

}
