package com.tiqwab.example.app

import com.tiqwab.example.modeling.{Iso, LongIdLike}
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary

trait Gens {

  def positiveLongGen: Gen[Long] =
    Gen.chooseNum(0, Long.MaxValue)

  def longIdGen[A <: LongIdLike](implicit iso: Iso[Long, A]): Gen[A] =
    positiveLongGen.map(iso.get(_))

  lazy val applicationIdGen: Gen[ApplicationId] =
    longIdGen[ApplicationId]

  lazy val applicationGen: Gen[Application] =
    for {
      id <- applicationIdGen
      name <- arbitrary[String]
    } yield Application(id, name)

}

object Gens extends Gens
