package example

import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._

object CirceSample {

  case class Person(name: String, age: Int)
  implicit val personDecoder: Decoder[Person] = deriveDecoder[Person]
  implicit val personEncoder: Encoder[Person] = deriveEncoder[Person]

  val decoder1: Decoder[Person] = Decoder { json =>
    for {
      name <- json.downField("name").as[String]
      age <- json.downField("age").as[Int]
    } yield Person(name, age)
  }

  val json: Json = Json.obj(
    "name" -> "Alice".asJson,
    "age" -> 21.asJson
  )

  val sample1 = json.as[Person](decoder1)
}
