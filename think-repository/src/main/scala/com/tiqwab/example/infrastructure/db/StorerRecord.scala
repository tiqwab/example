package com.tiqwab.example.infrastructure.db

import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapper}

case class StorerRecord(
  id: Long,
  name: String
) {

}

object StorerRecord extends SkinnyCRUDMapper[StorerRecord] {

  override def defaultAlias: Alias[StorerRecord] = createAlias("storer")

  override def schemaName: Option[String] = Some("public")

  override def tableName: String = "storer"

  override def extract(rs: WrappedResultSet, n: ResultName[StorerRecord]): StorerRecord = {
    StorerRecord(
      rs.get(n.id),
      rs.get(n.name)
    )
  }
}
