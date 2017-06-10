package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Order, OrderId, Storer, StorerId}
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

  it should "store a new order" in { implicit session =>
    withContext { implicit ctx =>
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val order = Order(OrderId(genId), storer, DateTime.now())
      val orderTry = OrderRepository.ofJDBC.save(order)
      orderTry.isSuccess shouldBe true
    }
  }

  it should "store the order and update" in { implicit session =>
    withContext { implicit ctx =>
      val id = OrderId(genId)
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val order = Order(id, storer, DateTime.now())
      OrderRepository.ofJDBC.save(order)
      OrderRepository.ofJDBC.save(order.copy(orderDate = order.orderDate.minusDays(1)))
      OrderRepository.ofJDBC.findById(id) match {
        case Success(newOrder) => newOrder.orderDate shouldBe order.orderDate.minusDays(1)
        case Failure(e) => fail(s"Cannot find by id", e)
      }
    }
  }

  it should "find a order if exists" in { implicit session =>
    withContext { implicit ctx =>
      val id = genId
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val order = OrderRepository.ofJDBC.save(
        Order(OrderId(id), storer, DateTime.now())
      ).success.get
      val orderTry = OrderRepository.ofJDBC.findById(OrderId(id))
      orderTry.isSuccess shouldBe true
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
      val id = genId
      val storer = StorerRepository.ofJDBC.findById(StorerId(1)).success.value
      val order = OrderRepository.ofJDBC.save(
        Order(OrderId(id), storer, DateTime.now())
      ).success.get
      val orderTry = OrderRepository.ofJDBC.deleteById(OrderId(id))
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
