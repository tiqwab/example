package com.tiqwab.example.infrastructure.db

import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapper}

case class OrderDetailRecord(
  id: Long,
  orderId: Option[Long] = None,
  sku: String,
  qty: Int
)

object OrderDetailRecord extends SkinnyCRUDMapper[OrderDetailRecord] {

  override def defaultAlias: Alias[OrderDetailRecord] = createAlias("orderdetail")

  override def schemaName: Option[String] = Some("public")

  override def tableName: String = "orderdetail"

  override def extract(rs: WrappedResultSet, n: ResultName[OrderDetailRecord]): OrderDetailRecord = {
    OrderDetailRecord(
      id = rs.get(n.id),
      orderId = rs.get(n.orderId),
      sku = rs.get(n.sku),
      qty = rs.get(n.qty)
    )
  }

}
