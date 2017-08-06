package com.tiqwab.example.json.model

import play.api.libs.json.Json

case class FamilyMember(name: String, relation: String)

object FamilyMember {
  implicit val format = Json.format[FamilyMember]
}
