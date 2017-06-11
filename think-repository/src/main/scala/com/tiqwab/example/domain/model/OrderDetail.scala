package com.tiqwab.example.domain.model

import com.tiqwab.example.domain.support.Entity

case class OrderDetail(
  id: OrderDetailId,
  orderId: OrderId,
  sku: String,
  qty: Int
) extends Entity[OrderDetailId] {

  override type This = OrderDetail

}
