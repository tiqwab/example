package com.tiqwab.example.modeling

trait IdLike {
  type Value
  def value: Value
}

trait LongIdLike extends IdLike {
  override type Value = Long
}
