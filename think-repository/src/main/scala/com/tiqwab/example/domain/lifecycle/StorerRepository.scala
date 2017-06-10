package com.tiqwab.example.domain.lifecycle

import com.tiqwab.example.domain.model.{Storer, StorerId}
import com.tiqwab.example.domain.support.Repository

trait StorerRepository extends Repository[StorerId, Storer] {

}

object StorerRepository {

  def ofJDBC: StorerRepository =
    new StorerRepositoryOnJDBC()

}
