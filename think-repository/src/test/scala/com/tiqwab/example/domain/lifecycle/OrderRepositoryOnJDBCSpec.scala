package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model._
import com.tiqwab.example.domain.support.{EntityIOContext, EntityIOContextOnJDBC, EntityNotFoundException}
import com.tiqwab.example.infrastructure.identifier.IdentifierService
import org.joda.time.DateTime
import org.scalatest.fixture.FlatSpec
import org.scalatest.Matchers
import scalikejdbc.DBSession
import scalikejdbc.config._
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest.TryValues._

import scala.util.{Failure, Success}

class OrderRepositoryOnJDBCSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "OrderRepositoryOnJDBC"

  override def fixture(implicit session: DBSession): Unit = {
    withContext { implicit ctx =>
      val storer = StorerRepository.ofJDBC.save(Storer(StorerId(1), "DEMO1")).success.value
    }
  }

  def genId: Long = IdentifierService().generate

  def withContext[A](f: (EntityIOContext) => A)(implicit session: DBSession): A =
    f(EntityIOContextOnJDBC(session))

  it should "store a new order with a detail" in { implicit session =>
    withContext { implicit ctx =>
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val orderId = OrderId(genId)
      val orderDetails = Seq(
        OrderDetail(id = OrderDetailId(genId), orderId = orderId, sku = "SKU001", qty = 1)
      )
      val order = Order(
        id = orderId,
        storer = storer,
        orderDate = DateTime.now(),
        details = orderDetails
      )
      val orderTry = OrderRepository.ofJDBC.save(order)
      orderTry.isSuccess shouldBe true
    }
  }

  it should "store the order with detail and update" in { implicit session =>
    withContext { implicit ctx =>
      val orderId = OrderId(genId)
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val orderDetails = Seq(
        OrderDetail(id = OrderDetailId(genId), orderId = orderId, sku = "SKU001", qty = 1)
      )
      val order = Order(
        id = orderId,
        storer = storer,
        orderDate = DateTime.now(),
        details = orderDetails
      )
      OrderRepository.ofJDBC.save(order)
      OrderRepository.ofJDBC.save(order.copy(
        orderDate = order.orderDate.minusDays(1),
        details = order.details.map(_.copy(qty = 2))
      ))
      OrderRepository.ofJDBC.findById(orderId) match {
        case Success(newOrder) =>
          newOrder.orderDate shouldBe order.orderDate.minusDays(1)
          newOrder.details(0).qty shouldBe 2
        case Failure(e) => fail(s"Cannot find by id: ${e.getMessage}", e)
      }
    }
  }

  it should "find a order if exists" in { implicit session =>
    withContext { implicit ctx =>
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val orderId = OrderId(genId)
      val orderDetails = Seq(
        OrderDetail(id = OrderDetailId(genId), orderId = orderId, sku = "SKU001", qty = 1)
      )
      val order = OrderRepository.ofJDBC.save(
        Order(
          id = orderId,
          storer = storer,
          orderDate = DateTime.now(),
          details = orderDetails
        )
      ).success.get
      val orderTry = OrderRepository.ofJDBC.findById(orderId)
      orderTry.isSuccess shouldBe true
      orderTry.success.value.details.size shouldBe 1
    }
  }

  it should "find nothing if not exist" in { implicit session =>
    withContext { implicit ctx =>
      val orderTry = OrderRepository.ofJDBC.findById(OrderId(2))
      orderTry.failure.exception shouldBe a [EntityNotFoundException]
    }
  }

  it should "delete a order if exists" in { implicit session =>
    withContext { implicit ctx =>
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val orderId = OrderId(genId)
      val orderDetails = Seq(
        OrderDetail(id = OrderDetailId(genId), orderId = orderId, sku = "SKU001", qty = 1)
      )
      val order = OrderRepository.ofJDBC.save(
        Order(
          id = orderId,
          storer = storer,
          orderDate = DateTime.now(),
          details = orderDetails
        )
      ).success.get
      val orderTry = OrderRepository.ofJDBC.deleteById(orderId)
      orderTry.isSuccess shouldBe true
    }
  }

  it should "delete nothing if not exist" in { implicit session =>
    withContext { implicit ctx =>
      val orderTry = OrderRepository.ofJDBC.deleteById(OrderId(2))
      orderTry.failure.exception shouldBe a [EntityNotFoundException]
    }
  }

}
