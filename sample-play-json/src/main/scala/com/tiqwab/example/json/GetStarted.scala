package com.tiqwab.example.json

import play.api.libs.json._

object GetStarted extends App {
  /*
   * 1. Convert between String and Json simply
   */
  val rawString =
    """
      |{
      |  "name": "Alice",
      |  "age": 20,
      |  "favorites": ["tennis", "swimming"],
      |  "family": [
      |    {
      |      "name": "Bob",
      |      "relation": "father"
      |    },
      |    {
      |      "name": "Catharin",
      |      "relation": "mother"
      |    }
      |  ]
      |}
    """.stripMargin

  // `Json.parse` throws exception for invalid input
  val json: JsValue = Json.parse(rawString)
  println(s"parsed json: $json")
  println(s"reversed to string: ${Json.stringify(json)}") // or use Json.prettyPrint(json)

  /*
   * 3. Traverse json
   */
  // Traverse json by `\`
  // JsLookupResult is `data JsLookupResult = JsDefined JsValue | JsUndefined String`
  val lookupName: JsLookupResult = json \ "name"
  println(lookupName.get) // "Alice"

  val lookupUnknown = json \ "xxx"
  lookupUnknown match {
    case JsDefined(value) =>
      println(value)
    case err @ JsUndefined() =>
      println(err) // JsUndefined('xxx'List([{"name":"Bob","relation":"father"},{"name":"Catharin","relation":"mother"}]) is undefined on object: {"name":"Alice","age":20,"favorites":["tennis","swimming"],"family":[{"name":"Bob","relation":"father"},{"name":"Catharin","relation":"mother"}]})
  }

  // `\` can be repeated
  println(json \ "favorites" \ 1) // JsDefined("swimming")
  println(json \ "xxx" \ "yyy") // JsUndefined('xxx' is undefined on object: {"name":"Alice","age":20,"favorites":["tennis","swimming"],"family":[{"name":"Bob","relation":"father"},{"name":"Catharin","relation":"mother"}]})

  // Retrieve values by `\\`
  val selectFamily: Seq[JsValue] = json \\ "family"
  println(selectFamily) // List([{"name":"Bob","relation":"father"},{"name":"Catharin","relation":"mother"}])
  println(json \\ "xxx") // List()

  /*
   * 4. Reads, Writes, Format
   */
  // Reads
  val nameJson = Json.obj("name" -> "Alice")
  println(nameJson.as[Name].value) // throw exception if fails
  println(nameJson.validate[Name]) // it is safer to use 'validate'
  println(Json.fromJson[Name](nameJson))
  val illegalNameJson = Json.obj("xxx" -> "Alice")
  println(illegalNameJson.validate[Name])
  println(Json.fromJson[Name](illegalNameJson))

  val ageJson = Json.obj("age" -> 20)
  println(ageJson.validate[Age])

  // Writes
  val name = new Name("Alice")
  println(Json.toJson(name))
  val age = Age(20)
  println(Json.toJson(age))
}
