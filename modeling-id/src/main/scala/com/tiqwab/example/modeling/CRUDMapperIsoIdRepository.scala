package com.tiqwab.example.modeling

import skinny.orm.SkinnyMapperWithId
import skinny.orm.feature.CRUDFeatureWithId

abstract class CRUDMapperIsoIdRepository[ID, DbID, E <: Entity[ID]](
    implicit val iso: Iso[DbID, ID]
) extends SkinnyMapperWithId[ID, E]
    with CRUDFeatureWithId[ID, E]
    with RepositoryOnJdbc[ID, E] {

  override def useAutoIncrementPrimaryKey: Boolean = false

  override def idToRawValue(id: ID): Any =
    iso.reverseGet(id)

  override def rawValueToId(value: Any): ID =
    iso.get(value.asInstanceOf[DbID])

}
