package controllers

import models.Iso
import play.api.mvc.PathBindable

trait CustomPathBindable {

  implicit def isoPathBindable[A, B](
      implicit iso: Iso[A, B],
      bindable: PathBindable[A]): PathBindable[B] =
    bindable.transform(iso.get, iso.reverseGet)

}

object CustomPathBindable extends CustomPathBindable
