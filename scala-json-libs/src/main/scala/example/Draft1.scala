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
      JsObject(
        Map(
          "name" -> JsString("Alice"),
          "age" -> JsNumber(BigDecimal("20"))
        ))

    lazy val sample2: JsValue =
      Json.obj(
        "name" -> "Alice",
        "age" -> 20
      )

    lazy val sample3: JsValue =
      Json.obj(
        "name" -> stringToJsString("Alice"),
        "age" -> intToJsNumber(20)
      )
  }

  case class JsLookup(result: JsLookupResult) {
    def \(key: String): JsLookupResult = result match {
      case JsDefined(JsObject(kv)) if kv.contains(key) => JsDefined(kv(key))
      case _                                           => JsUndefined("not found")
    }
  }

  sealed trait JsLookupResult {
    def \(key: String): JsLookupResult = this match {
      case JsDefined(_)           => JsLookup(this) \ key
      case undef @ JsUndefined(_) => undef
    }

    def toOption: Option[JsValue] = this match {
      case JsDefined(json) => Some(json)
      case JsUndefined(_)  => None
    }
  }
  case class JsDefined(result: JsValue) extends JsLookupResult
  case class JsUndefined(err: String) extends JsLookupResult

  lazy val lookup1: Option[JsValue] = (JsLookup(JsDefined(Json.sample2)) \ "name").toOption
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

  case class Person1(name: String, age: Int)

  lazy val personReads1: Reads[Person1] = (
    (JsPath \ "name").read[String] ~
      (JsPath \ "age").read[Int]
  )((name, age) => Person1(name, age)) // same as `Person1.apply _`

  // Reads[A] represents `JsValue => A`
  // It should show success or failure
  lazy val stringReads1: Reads[String] = Reads { json =>
    json match { // this pattern match is redundant
      case JsString(string) => JsSuccess(string, JsPath())
      case _                => JsError("error")
    }
  }

  // Path to a JsValue
  lazy val path1: JsPath = JsPath \ "address" \ "city"
  lazy val readPath1: JsResult[JsValue] = path1.asSingleJsResult(sample1) // JsSuccess("Tokyo",)
  lazy val path2: JsPath = JsPath \ "foo"
  lazy val readPath2
    : JsResult[JsValue] = path2.asSingleJsResult(sample1) // JsError(List((/foo,List(JsonValidationError(List(error.path.missing),WrappedArray())))))
  lazy val path3: JsPath = JsPath \ "foo" \ "city"
  lazy val readPath3
    : JsResult[JsValue] = path3.asSingleJsResult(sample1) // JsError(List((/foo/city,List(JsonValidationError(List(error.path.missing),WrappedArray())))))

  // (JsPath \ "name").read[String]
  lazy val name1Reads: Reads[String] = Reads { json =>
    val result = (JsPath \ "name").asSingleJsResult(json)
    result match {
      case JsSuccess(jsValue, _) =>
        jsValue match {
          case JsString(str) => JsSuccess(str)
          case _             => JsError("error")
        }
      case err @ JsError(_) => err
    }
  }

  // accept Reads[String]
  // In other words, JsPath defines where we want to read, and the accepted Reads defines how we want to read, respectively
  def name2Reads(stringReads: Reads[String]): Reads[String] = Reads { json =>
    val result = (JsPath \ "name").asSingleJsResult(json)
    result match {
      case JsSuccess(jsValue, _) => stringReads.reads(jsValue)
      case err @ JsError(_)      => err
    }
  }

  def name3Reads(stringReads: Reads[String]): Reads[String] = Reads { json =>
    for {
      result <- (JsPath \ "name").asSingleJsResult(json)
      str <- stringReads.reads(result)
    } yield str
  }

  // ... then change it to a method of JsPath and accept Reads implicitly

  case class ReadsCombinator2[A, B](ra: Reads[A], rb: Reads[B]) {
    def apply[C](f: (A, B) => C): Reads[C] = Reads { json =>
      (ra.reads(json), rb.reads(json)) match {
        case (JsSuccess(a, _), JsSuccess(b, _)) =>
          JsSuccess(f(a, b))
        case _ =>
          JsError("error")
      }
    }

    def ~[C](rc: Reads[C]): ReadsCombinator3[A, B, C] = ReadsCombinator3(ra, rb, rc)
  }

  case class ReadsCombinator3[A, B, C](ra: Reads[A], rb: Reads[B], rc: Reads[C]) {
    def apply[D](f: (A, B, C) => D): Reads[D] = Reads { json =>
      (ra.reads(json), rb.reads(json), rc.reads(json)) match {
        case (JsSuccess(a, _), JsSuccess(b, _), JsSuccess(c, _)) =>
          JsSuccess(f(a, b, c))
        case _ =>
          JsError("error")
      }
    }
  }

  // ... then define ReadsCombinator4, ... ReadsCombinator22

  lazy val personReads2: Reads[Person1] = ReadsCombinator2(
    (JsPath \ "name").read[String],
    (JsPath \ "age").read[Int]
  )((name, age) => Person1(name, age))

  // ... then change it to a method of Reads
  // actually it is generalized for Reads, Writes, and Format

  case class Address(city: String)
  case class Person2(name: String, age: Int, address: Address)

  val json = Json.parse("""{"name": "Alice", "age": 21}""")
  val readsSample1 = personReads2.reads(json).get

  val json2 = Json.parse("""{"name": "Alice", "age": 21}""")
  val str2 = Json.stringify(json2)

}
