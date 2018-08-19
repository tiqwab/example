package com.tiqwab.example.scalacheck

import org.scalacheck._

class SampleCheck extends Properties("SampleCheck") {
  import Gens._

  property("simple check") = Prop.forAll(smallIntGen, smallIntGen) { (x: Int, y: Int) =>
    x + y == y + x
  }

  {
    implicit val smallIntArg: Arbitrary[Int] = Arbitrary(smallIntGen)
    property("simple check with implicit Arbitrary") = Prop.forAll { (x: Int, y: Int) =>
      x + y == y + x
    }
  }

  property("simple check and collect generated data1") = Prop.forAll(smallIntGen, smallIntGen) { (x: Int, y: Int) =>
    Prop.collect((x, y)) {
      x + y == y + x
    }
  }

  property("simple check and classify generated data2") = Prop.forAll(smallIntGen, smallIntGen) { (x: Int, y: Int) =>
    Prop.classify(x > 5, "large x") {
      Prop.classify(y > 5, "large y") {
        x + y == y + x
      }
    }
  }

  property("person without shrink") = Prop.forAll(personGen, personGen) { (p1: Person, p2: Person) =>
    Prop.classify(p1.id == p2.id, "same") {
      Prop.classify(p1.id != p2.id, "different") {
        if (p1.id == p2.id) {
          p1 == p2
        } else {
          p1 != p2
        }
      }
    }
  }

  {
    implicit def personShrink(implicit stringShrink: Shrink[String], intShrink: Shrink[Int]): Shrink[Person] =
      Shrink.apply { p =>
        (for { n <- Shrink.shrink(p.name) } yield p.copy(name = n)) append
          (for { a <- Shrink.shrink(p.age) } yield p.copy(age = a))
      }

    property("person with shrink") = Prop.forAll(personGen, personGen) { (p1: Person, p2: Person) =>
      Prop.classify(p1.id == p2.id, "same") {
        Prop.classify(p1.id != p2.id, "different") {
          if (p1.id == p2.id) {
            p1 == p2
          } else {
            p1 != p2
          }
        }
      }
    }
  }

}
