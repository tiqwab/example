package com.tiqwab.example.scalacheck

import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

trait Gens {

  def smallIntGen: Gen[Int] = Gen.choose(1, 10)

  def personGen: Gen[Person] =
    for {
      id <- arbLong.arbitrary.filter(_ > 0)
      name <- Gen.alphaNumStr
      age <- Gen.choose(0, 200)
    } yield Person(id, name, age)

}

object Gens extends Gens
