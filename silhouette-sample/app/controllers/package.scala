import play.api.libs.json.{JsError, Reads}
import play.api.mvc.{BodyParser, BodyParsers}
import play.api.mvc.Results._

package object controllers {

  // Iteratee ref: https://www.slideshare.net/TakashiKawachi/iteratee
  def jsonParser[A: Reads]: BodyParser[A] = BodyParser("json reader") {
    request =>
      import play.api.libs.iteratee.Execution.Implicits.trampoline
      BodyParsers.parse
        .tolerantJson(request)
        .map(_.right.flatMap { jsValue =>
          jsValue
            .validate(implicitly[Reads[A]])
            .map(Right.apply)
            .recoverTotal { jsError =>
              Left(BadRequest(s"invalid json: ${JsError.toJson(jsError)}"))
            }
        })
  }

}
