package models.authentication.lifecycle

import models.{Entity, IdLike, Iso, Repository}
import scalikejdbc.DBSession
import scalikejdbc.interpolation.SQLSyntax
import skinny.orm.SkinnyCRUDMapperWithId

trait CRUDMapperWithId[ID <: IdLike, E <: Entity[ID]] {

  this: Repository[ID, E, DBSession] with SkinnyCRUDMapperWithId[ID, E] =>

  def iso: Iso[ID#Value, ID]
  def namedValues(entity: E): Seq[(SQLSyntax, Any)]

  override def useAutoIncrementPrimaryKey: Boolean = false

  override def idToRawValue(id: ID): Any =
    iso.reverseGet(id)

  override def rawValueToId(value: Any): ID =
    iso.get(value.asInstanceOf[ID#Value])

  override def store(entity: E)(implicit context: DBSession): Unit = {
    if (findById(entity.id).isDefined) {
      updateById(entity.id).withNamedValues(namedValues(entity): _*)
    } else {
      createWithNamedValues(namedValues(entity): _*)
    }
  }

}
