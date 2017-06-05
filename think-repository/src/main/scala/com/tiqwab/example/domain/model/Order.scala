package com.tiqwab.example.domain.model

import com.tiqwab.example.domain.support.Entity
import org.joda.time.DateTime

case class Order(
  id: OrderId,
  storer: Storer,
  orderDate: DateTime
) extends Entity[OrderId] {

  override type This = Order

}
