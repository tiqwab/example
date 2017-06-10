package com.tiqwab.example.infrastructure.db

import org.joda.time.DateTime
import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapper}

case class OrderRecord(
  id: Long,
  storerkey: String,
  orderDate: DateTime
)

object OrderRecord extends SkinnyCRUDMapper[OrderRecord] {

  override def defaultAlias: Alias[OrderRecord] = createAlias("orders")

  override def schemaName: Option[String] = Some("public")

  override def tableName: String = "orders"

  override def extract(rs: WrappedResultSet, n: ResultName[OrderRecord]): OrderRecord = {
    OrderRecord(
      rs.get(n.id),
      rs.get(n.storerkey),
      rs.get(n.orderDate)
    )
  }

}
