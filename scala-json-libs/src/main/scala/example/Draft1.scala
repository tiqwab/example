package example

object Draft1 {
  sealed trait JsValue

  case object JsNull extends JsValue
  case class JsBoolean(value: Boolean) extends JsValue // JsTrue and JsFalse objects exists actually
  case class JsString(value: String) extends JsValue
  case class JsNumber(value: BigDecimal) extends JsValue // Use BigDecimal
  case class JsArray(value: IndexedSeq[JsValue]) extends JsValue // it would be better to use IndexedSeq not LinearSeq
  case class JsObject(value: Map[String, JsValue]) extends JsValue

  // using implicit conversion
  object Json {
    import scala.languageFeature.implicitConversions

    def obj(kv: (String, JsValue)*): JsObject =
      JsObject(kv.toMap)

    // Json.obj accepts JsValueWrapper actually instead of JsValue and implicit conversion from Writes to JsValueWrapper

    implicit def stringToJsString(value: String): JsValue =
      JsString(value)

    implicit def intToJsNumber(value: Int): JsValue =
      JsNumber(BigDecimal(value))

    lazy val sample1: JsValue =
      Json.obj(
        "name" -> JsString("Alice"),
        "age" -> JsNumber(BigDecimal("20"))
      )

    lazy val sample2: JsValue =
      Json.obj(
        "name" -> "Alice",
        "age" -> 20
      )
  }
}

object Draft2 {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  lazy val sample1: JsValue = Json.parse("""
      |{
      |  "name": "Alice",
      |  "age": 21,
      |  "address": {
      |    "city": "Tokyo"
      |  }
      |}
    """.stripMargin)
  lazy val str1: String = Json.stringify(sample1)

  lazy val traverseValue1
    : Option[JsValue] = (sample1 \ "name").toOption // res0: Option[play.api.libs.json.JsValue] = Some("Alice")
  lazy val traverseValue2
    : Option[JsValue] = (sample1 \ "address" \ "city").toOption // res1: Option[play.api.libs.json.JsValue] = Some("Tokyo")

  JsValue.jsValueToJsLookup()

  /*
  def nameAndCity(json: JsObject): Option[(String, String)] =
    for {
      name <- (json \ "name").toOption
      city <- (json \ "age").toOption
    } yield (name, city)
   */

  lazy val reads1: Reads[String] = (JsPath \ "name").read[String]
  lazy val reads2: Reads[Int] = (JsPath \ "age").read[Int]

  lazy val sample2: JsValue = Json.obj(
    "name" -> "Alice",
    "age" -> 21
  )

  println(reads1.reads(sample2))
  println(reads2.reads(sample2))

  lazy val reads3: Reads[(String, Int)] = (
    (JsPath \ "name").read[String] ~
      (JsPath \ "age").read[Int]
  )((name, age) => (name, age))

}
