package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model._
import com.tiqwab.example.domain.support.{EntityNotFoundException, RepositoryOnJDBC}
import com.tiqwab.example.infrastructure.db.{OrderDetailRecord, OrderRecord}

import scala.util.Try

class OrderRepositoryOnJDBC extends OrderRepository with RepositoryOnJDBC[OrderId, Order] {

  override def save(entity: Order)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    Try {
      val id = entity.id.value
      val storerId = entity.storer.id.value
      val orderDate = entity.orderDate
      val details = entity.details

      val c = OrderRecord.column

      val updateOrderResult = OrderRecord.updateById(id).withNamedValues(
        c.storerId -> storerId,
        c.orderDate -> orderDate
      )
      if (updateOrderResult == 0) {
        OrderRecord.createWithNamedValues(
          c.id -> id,
          c.storerId -> storerId,
          c.orderDate -> orderDate
        )
      }

      details.foreach { detail =>
        OrderDetailRepository.ofJDBC.save(detail)
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
          Order(
            id = OrderId(order.id),
            storer = Storer(StorerId(storer.id), storer.name),
            details = order.details.map { record =>
              OrderDetail(
                id = OrderDetailId(record.id),
                orderId = OrderId(record.orderId.getOrElse(throw new IllegalStateException(s"The detail in the order (${order.id}) has no orderId"))),
                sku = record.sku,
                qty = record.qty
              )
            },
            orderDate = order.orderDate
          )
        }
      orderOpt.getOrElse(throw EntityNotFoundException(id))
    }
  }

  override def deleteById(id: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    for {
      order <- findById(id)
    } yield {
      order.details.foreach { record => OrderDetailRepository.ofJDBC.deleteById(record.id) }
      OrderRecord.deleteById(id.value)
      order
    }
  }

}
