package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Storer, StorerId}
import com.tiqwab.example.domain.support.{EntityIOContext, EntityIOContextOnJDBC}
import com.tiqwab.example.infrastructure.identifier.IdentifierService
import org.scalatest.Matchers
import org.scalatest.fixture.FlatSpec
import scalikejdbc.DBSession
import scalikejdbc.config.DBs
import scalikejdbc.scalatest.AutoRollback

class StorerRepositoryOnJDBCSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "StorerRepositoryOnJDBC"

  override def fixture(implicit session: DBSession): Unit = {

  }

  def genId = IdentifierService().generate

  def withContext[A](f: (EntityIOContext) => A)(implicit session: DBSession): A =
    f(EntityIOContextOnJDBC(session))

  it should "store a new storer" in { implicit session =>
    withContext { implicit ctx =>
      val storer = Storer(StorerId(genId), "DEMO1")
      val storerTry = StorerRepository.ofJDBC.save(storer)
      storerTry.isSuccess shouldBe true
    }
  }

  it should "find a storer if exists" in { implicit session =>
    withContext { implicit ctx =>
      val id = StorerId(genId)
      val storer = Storer(id, "DEMO1")
      StorerRepository.ofJDBC.save(storer)
      val storerTry = StorerRepository.ofJDBC.findById(id)
      storerTry.isSuccess shouldBe true
    }
  }

}
