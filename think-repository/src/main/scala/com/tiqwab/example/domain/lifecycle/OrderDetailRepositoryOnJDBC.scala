package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{OrderDetail, OrderDetailId, OrderId}
import com.tiqwab.example.domain.support.{EntityNotFoundException, RepositoryOnJDBC}
import com.tiqwab.example.infrastructure.db.OrderDetailRecord

import scala.util.Try

class OrderDetailRepositoryOnJDBC extends OrderDetailRepository with RepositoryOnJDBC[OrderDetailId, OrderDetail] {

  override def save(entity: OrderDetail)(implicit ctx: Ctx): Try[OrderDetail] = withDBSession(ctx) { implicit session =>
    Try {
      val id = entity.id.value
      val orderId = entity.orderId.value
      val sku = entity.sku
      val qty = entity.qty

      val c = OrderDetailRecord.column

      val updateResult = OrderDetailRecord.updateById(id).withNamedValues(
        c.orderId -> orderId,
        c.sku -> sku,
        c.qty -> qty
      )
      if (updateResult == 0) {
        OrderDetailRecord.createWithNamedValues(
          c.id -> id,
          c.orderId -> orderId,
          c.sku -> sku,
          c.qty -> qty
        )
      }
      entity
    }
  }

  override def findById(id: OrderDetailId)(implicit ctx: Ctx): Try[OrderDetail] = {
    Try {
      val orderDetailOpt = for {
        od <- OrderDetailRecord.findById(id.value)
        orderId <- od.orderId
      } yield {
        OrderDetail(OrderDetailId(od.id), OrderId(orderId), od.sku, od.qty)
      }
      orderDetailOpt.getOrElse(throw EntityNotFoundException(id))
    }
  }

  override def deleteById(id: OrderDetailId)(implicit ctx: Ctx): Try[OrderDetail] = {
    for {
      od <- findById(id)
    } yield {
      OrderDetailRecord.deleteById(id.value)
      od
    }
  }

}
