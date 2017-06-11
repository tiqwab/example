package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{OrderDetail, OrderDetailId}
import com.tiqwab.example.domain.support.Repository

trait OrderDetailRepository extends Repository[OrderDetailId, OrderDetail]

object OrderDetailRepository {

  def ofJDBC: OrderDetailRepository =
    new OrderDetailRepositoryOnJDBC()

}
