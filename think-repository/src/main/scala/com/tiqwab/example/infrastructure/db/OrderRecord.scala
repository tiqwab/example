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

  override def extract(rs: WrappedResultSet, orders: ResultName[OrderRecord]): OrderRecord = {
    OrderRecord(
      rs.get(orders.id),
      rs.get(orders.storerkey),
      rs.get(orders.orderDate)
    )
  }

}
