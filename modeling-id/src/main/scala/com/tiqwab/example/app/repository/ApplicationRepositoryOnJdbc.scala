package com.tiqwab.example.app.repository

import com.tiqwab.example.app.{Application, ApplicationId}
import com.tiqwab.example.modeling.CRUDMapperIsoIdRepository
import scalikejdbc.WrappedResultSet
import scalikejdbc.interpolation.SQLSyntax
import skinny.orm.Alias

class ApplicationRepositoryOnJdbc(override val tableName: String)
    extends CRUDMapperIsoIdRepository[ApplicationId, Long, Application] {

  override def defaultAlias: Alias[Application] = createAlias("AP")

  override protected def namedValuesWithoutId(
      entity: Application): Seq[(SQLSyntax, Any)] = Seq(
    column.name -> entity.name
  )

  override def extract(rs: WrappedResultSet,
                       n: scalikejdbc.ResultName[Application]): Application =
    Application(
      id = ApplicationId(rs.get(n.id)),
      name = rs.get(n.name)
    )

}
