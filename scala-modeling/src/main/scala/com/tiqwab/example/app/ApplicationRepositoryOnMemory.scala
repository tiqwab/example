package com.tiqwab.example.app

import com.tiqwab.example.modeling.RepositoryOnMemory

import scala.collection.mutable

class ApplicationRepositoryOnMemory
    extends RepositoryOnMemory[ApplicationId,
                               Application,
                               ApplicationRepositoryOnMemory.Context.type] {
  override protected val entities: mutable.Map[ApplicationId, Application] =
    scala.collection.mutable.Map.empty[ApplicationId, Application]
}

object ApplicationRepositoryOnMemory {
  object Context
  implicit val context: Context.type = Context
}
