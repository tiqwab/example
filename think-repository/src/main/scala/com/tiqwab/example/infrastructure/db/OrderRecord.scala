package com.tiqwab.example.infrastructure.db

import org.joda.time.DateTime
import scalikejdbc._
import skinny.orm.{Alias, SkinnyCRUDMapper}

case class OrderRecord(
  id: Long,
  storerId: Long,
  storer: Option[StorerRecord] = None,
  orderDate: DateTime
)

object OrderRecord extends SkinnyCRUDMapper[OrderRecord] {

  override def defaultAlias: Alias[OrderRecord] = createAlias("orders")

  override def schemaName: Option[String] = Some("public")

  override def tableName: String = "orders"

  belongsToWithFk[StorerRecord](right = StorerRecord, fk = "storer_id", merge = (order, storer) => order.copy(storer = storer)).byDefault

  override def extract(rs: WrappedResultSet, n: ResultName[OrderRecord]): OrderRecord = {
    OrderRecord(
      id = rs.get(n.id),
      storerId = rs.get(n.storerId),
      orderDate = rs.get(n.orderDate)
    )
  }

}
