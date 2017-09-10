package models

trait IsoIdCompanion[ID <: IdLike] {
  def apply(value: ID#Value): ID

  implicit val iso: Iso[ID#Value, ID] = Iso(apply, _.value)
}
