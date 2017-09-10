package controllers

import models.Iso
import models.authentication.Email
import play.api.mvc.QueryStringBindable
import play.api.mvc.QueryStringBindable.Parsing

trait CustomQueryStringBindable {

  implicit val emailQueryStringBindable: QueryStringBindable[Email] =
    new Parsing[Email](
      Email(_),
      _.value,
      (key: String, e: Exception) => s"Cannot parse parameter $key as Email"
    )

  implicit def isoQueryStringBindable[A, B](
      implicit iso: Iso[A, B],
      bindable: QueryStringBindable[A]
  ): QueryStringBindable[B] =
    bindable.transform(iso.get, iso.reverseGet)

}

object CustomQueryStringBindable extends CustomQueryStringBindable
