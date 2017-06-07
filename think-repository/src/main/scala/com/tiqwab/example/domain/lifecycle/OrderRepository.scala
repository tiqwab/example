package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Order, OrderId}
import com.tiqwab.example.domain.support.Repository

trait OrderRepository extends Repository[OrderId, Order] {

}

object OrderRepository {

  def ofJDBC: OrderRepository =
    new OrderRepositoryOnJDBC()

}
