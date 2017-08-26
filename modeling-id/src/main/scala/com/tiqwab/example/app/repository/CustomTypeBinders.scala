package com.tiqwab.example.app.repository

import java.sql.ResultSet

import com.tiqwab.example.app.ApplicationId
import com.tiqwab.example.modeling.{IdLike, Iso}
import scalikejdbc.TypeBinder

trait CustomTypeBinders {

  def isoBinder[ID <: IdLike](implicit iso: Iso[ID#Value, ID]): TypeBinder[ID] =
    new TypeBinder[ID] {
      override def apply(rs: ResultSet, columnIndex: Int): ID =
        iso.get(rs.getObject(columnIndex).asInstanceOf[ID#Value])
      override def apply(rs: ResultSet, columnLabel: String): ID =
        iso.get(rs.getObject(columnLabel).asInstanceOf[ID#Value])
    }

  implicit lazy val applicationIdTypeBinder: TypeBinder[ApplicationId] =
    isoBinder[ApplicationId]

}

object CustomTypeBinders extends CustomTypeBinders
