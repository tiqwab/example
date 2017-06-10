package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Order, OrderId, Storer, StorerId}
import com.tiqwab.example.domain.support.{EntityNotFoundException, RepositoryOnJDBC}
import com.tiqwab.example.infrastructure.db.OrderRecord

import scala.util.Try

class OrderRepositoryOnJDBC extends OrderRepository with RepositoryOnJDBC[OrderId, Order] {

  override def save(entity: Order)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    Try {
      val id = entity.id.value
      val storerId = entity.storer.id.value
      val orderDate = entity.orderDate

      val c = OrderRecord.column

      val updateResult = OrderRecord.updateById(id).withNamedValues(
        c.storerId -> storerId,
        c.orderDate -> orderDate
      )
      if (updateResult == 0) {
        OrderRecord.createWithNamedValues(
          c.id -> id,
          c.storerId -> storerId,
          c.orderDate -> orderDate
        )
      }
      entity
    }
  }

  override def findById(id: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    Try {
      val orderOpt =
        for {
          order <- OrderRecord.findById(id.value)
          storer <- order.storer
        } yield {
          Order(OrderId(order.id), Storer(StorerId(storer.id), storer.name), order.orderDate)
        }
      orderOpt.getOrElse(throw new EntityNotFoundException((s"$id")))
    }
  }

  override def deleteById(id: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    for {
      order <- findById(id)
    } yield {
      OrderRecord.deleteById(id.value)
      order
    }
  }

}
