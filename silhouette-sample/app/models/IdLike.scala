package models

trait IdLike {
  type Value
  def value: Value
}

trait LongIdLike extends IdLike {
  type Value = Long
}

trait StringIdLike extends IdLike {
  type Value = String
}
