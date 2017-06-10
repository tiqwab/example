package com.tiqwab.example.domain.lifecycle
import com.tiqwab.example.domain.model.{Storer, StorerId}
import com.tiqwab.example.domain.support.{EntityNotFoundException, RepositoryOnJDBC}
import com.tiqwab.example.infrastructure.db.StorerRecord

import scala.util.{Failure, Success, Try}

class StorerRepositoryOnJDBC extends StorerRepository with RepositoryOnJDBC[StorerId, Storer] {

  override def save(entity: Storer)(implicit ctx: Ctx): Try[Storer] = {
    Try {
      val id = entity.id.value
      val name = entity.name

      val c = StorerRecord.column

      val updateResult = StorerRecord.updateById(id).withNamedValues(
        c.name -> name
      )
      if (updateResult == 0) {
        StorerRecord.createWithNamedValues(
          c.id -> id,
          c.name -> name
        )
      }
      entity
    }
  }

  override def findById(id: StorerId)(implicit ctx: Ctx): Try[Storer] = {
    StorerRecord.findById(id.value) match {
      case Some(storer) => Success(
        Storer(StorerId(storer.id), storer.name)
      )
      case None => Failure(new EntityNotFoundException(s"$id"))
    }
  }

  override def deleteById(id: StorerId)(implicit ctx: Ctx): Try[Storer] = {
    for {
      storer <- findById(id)
    } yield {
      StorerRecord.deleteById(id.value)
      storer
    }
  }

}
