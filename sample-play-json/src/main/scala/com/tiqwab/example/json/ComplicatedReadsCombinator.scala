package com.tiqwab.example.json

import play.api.libs.json._

object ComplicatedReadsCombinator extends App {
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

  val json = Json.parse(rawString)
  val person = json.validate[com.tiqwab.example.json.model.Person].get
  println(person)
  println(Json.stringify(Json.toJson(person)))
}
