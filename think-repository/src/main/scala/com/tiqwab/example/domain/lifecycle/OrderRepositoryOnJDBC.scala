package com.tiqwab.example.domain.lifecycle
import com.tiqwab.example.domain.model.{Order, OrderId}
import com.tiqwab.example.domain.support.RepositoryOnJDBC

import scala.util.Try

class OrderRepositoryOnJDBC extends OrderRepository with RepositoryOnJDBC[OrderId, Order] {

  override def save(entity: Order)(implicit ctx: Ctx): Order = withDBSession(ctx) { session =>
    ???
  }

  override def findById(id: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { session =>
    ???t
  }

}
