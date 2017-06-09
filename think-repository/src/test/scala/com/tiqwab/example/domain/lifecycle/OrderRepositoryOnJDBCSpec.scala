package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Order, OrderId, Storer}
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

class OrderRepositoryOnJDBCSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "OrderRepositoryOnJDBC"

  override def fixture(implicit session: DBSession): Unit = {

  }

  def genId: Long = IdentifierService().generate

  def withContext[A](f: (EntityIOContext) => A)(implicit session: DBSession): A =
    f(EntityIOContextOnJDBC(session))

  it should "store a order" in { implicit session =>
    withContext { implicit ctx =>
      val order = Order(OrderId(genId), Storer("DEMO1"), DateTime.now())
      val orderTry = OrderRepository.ofJDBC.save(order)
      orderTry.isSuccess shouldBe true
    }
  }

  it should "find a order if exists" in { implicit session =>
    withContext { implicit ctx =>
      val id = genId
      OrderRepository.ofJDBC.save(
        Order(OrderId(id), Storer("DEMO1"), DateTime.now())
      )
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

}
