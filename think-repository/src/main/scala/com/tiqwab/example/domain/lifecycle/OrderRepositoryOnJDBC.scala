package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Order, OrderId, Storer}
import com.tiqwab.example.domain.support.{EntityNotFoundException, RepositoryOnJDBC}
import com.tiqwab.example.infrastructure.db.OrderRecord

import scala.util.Try

class OrderRepositoryOnJDBC extends OrderRepository with RepositoryOnJDBC[OrderId, Order] {

  override def save(entity: Order)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    Try {
      val id = entity.id.value
      val storer = entity.storer.name
      // val orderDate = entity.orderDate.toString("yyyy-MM-dd HH:mm:ss")
      /*
      val column = OrderRecord.column
      OrderRecord.createWithNamedValues(
        column.id -> id,
        column.storerkey -> storer,
        column.orderDate -> entity.orderDate
      )
      */
      OrderRecord.createWithAttributes(
        ('id, id),
        ('storerkey, storer),
        ('order_date, entity.orderDate)
      )
      entity
    }
  }

  override def findById(id: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    Try {
      OrderRecord.findById(id.value).map { record =>
        Order(OrderId(record.id), Storer(record.storerkey), record.orderDate)
      }.getOrElse(throw new EntityNotFoundException((s"$id")))
    }
  }

}
