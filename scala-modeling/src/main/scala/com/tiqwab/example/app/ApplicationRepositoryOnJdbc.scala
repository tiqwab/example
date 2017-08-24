package com.tiqwab.example.app

import com.tiqwab.example.modeling.CRUDMapperLongIdRepository
import com.tiqwab.example.app.CustomTypeBinders._
import scalikejdbc._
import scalikejdbc.interpolation.SQLSyntax
import skinny.orm.Alias

class ApplicationRepositoryOnJdbc(override val tableName: String)
    extends CRUDMapperLongIdRepository[ApplicationId, Application, Application] {

  override def defaultAlias: Alias[Application] = createAlias("AP")

  override def extract(rs: WrappedResultSet,
                       n: ResultName[Application]): Application = {
    new Application(
      rs.get(n.id),
      rs.get(n.name),
      rs.get(n.createdAt),
      rs.get(n.updatedAt)
    )
  }

  override protected def namedValuesWithoutId(
      entity: Application): Seq[(SQLSyntax, Any)] = Seq(
    column.name -> entity.name,
    column.createdAt -> entity.createdAt,
    column.updatedAt -> entity.updatedAt
  )

}
