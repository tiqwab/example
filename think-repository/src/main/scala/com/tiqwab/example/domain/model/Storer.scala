package com.tiqwab.example.domain.model

import com.tiqwab.example.domain.support.Entity

case class Storer(
  id: StorerId,
  name: String
) extends Entity[StorerId] {

  override type This = Storer

}
