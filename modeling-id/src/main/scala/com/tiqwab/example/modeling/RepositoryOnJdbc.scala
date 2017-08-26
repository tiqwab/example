package com.tiqwab.example.modeling

import scalikejdbc.{AsIsParameterBinder, DBSession}
import scalikejdbc.interpolation.SQLSyntax
import skinny.orm.feature.CRUDFeatureWithId

trait RepositoryOnJdbc[ID, E <: Entity[ID]]
    extends Repository[ID, E, DBSession] {
  this: CRUDFeatureWithId[ID, E] =>

  protected def namedValuesWithoutId(entity: E): Seq[(SQLSyntax, Any)]

  protected def namedValues(entity: E): Seq[(SQLSyntax, Any)] =
    idNamedValue(entity) +: namedValuesWithoutId(entity)

  protected def idNamedValue(entity: E): (SQLSyntax, Any) =
    column.id -> AsIsParameterBinder(idToRawValue(entity.id))

  override def store(entity: E)(implicit ctx: DBSession): E = {
    if (findById(entity.id).isDefined) {
      updateById(entity.id).withNamedValues(namedValuesWithoutId(entity): _*)
    } else {
      createWithNamedValues(namedValues(entity): _*)
    }
    entity
  }
}
