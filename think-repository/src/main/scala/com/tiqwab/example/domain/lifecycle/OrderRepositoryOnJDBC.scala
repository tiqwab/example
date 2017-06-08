package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Order, OrderId, Storer}
import com.tiqwab.example.domain.support.{EntityNotFoundException, RepositoryOnJDBC}
import scalikejdbc._
import scala.util.Try

class OrderRepositoryOnJDBC extends OrderRepository with RepositoryOnJDBC[OrderId, Order] {

  override def save(entity: Order)(implicit ctx: Ctx): Order = withDBSession(ctx) { implicit session =>
    ???
  }

  override def findById(id: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) { implicit session =>
    Try {
      sql"SELECT id, storerkey, order_date FROM orders WHERE id = ${id.value}".map { rs =>
        Order(OrderId(rs.long("id")), Storer(rs.string("storerkey")), rs.jodaDateTime("order_date"))
      }.single.apply()
        .getOrElse(throw new EntityNotFoundException(s"$id"))
    }
  }

}
