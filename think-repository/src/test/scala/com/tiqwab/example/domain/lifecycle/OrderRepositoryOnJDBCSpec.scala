package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Order, OrderId}
import com.tiqwab.example.domain.support.{EntityIOContext, EntityIOContextOnJDBC, EntityNotFoundException}
import org.scalatest.fixture.FlatSpec
import org.scalatest.Matchers
import scalikejdbc.DBSession
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import org.scalatest.TryValues._

class OrderRepositoryOnJDBCSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "OrderRepositoryOnJDBC"

  override def fixture(implicit session: DBSession): Unit = {
    sql"INSERT INTO orders (id, storerkey, order_date) VALUES (1, 'DEMO1', '2017-06-08 12:00:00')".update.apply()
  }

  def withContext[A](f: (EntityIOContext) => A)(implicit session: DBSession): A =
    f(EntityIOContextOnJDBC(session))

  it should "find a order if exists" in { implicit session =>
    withContext { implicit ctx =>
      val orderTry = OrderRepository.ofJDBC.findById(OrderId(1))
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
