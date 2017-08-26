package com.tiqwab.example.app

import com.tiqwab.example.modeling.Iso
import org.scalacheck.{Gen, Prop}

trait PropUtils {

  def isoLaws[A: Gen, B: Gen](implicit iso: Iso[A, B]): Prop = {
    val x = Prop.forAll(implicitly[Gen[A]]) { (a: A) =>
      iso.reverseGet(iso.get(a)) == a
    }
    val y = Prop.forAll(implicitly[Gen[B]]) { (b: B) =>
      iso.get(iso.reverseGet(b)) == b
    }
    x && y
  }

}

object PropUtils extends PropUtils
