package com.tiqwab.example.app

import com.tiqwab.example.modeling.Iso
import scalikejdbc.TypeBinder

trait CustomTypeBinders {

  def isoBinder[A, B](implicit binder: TypeBinder[A],
                      iso: Iso[A, B]): TypeBinder[B] =
    binder.map(iso.get)

  implicit def applicationIdTypeBinder: TypeBinder[ApplicationId] =
    isoBinder[Long, ApplicationId]

}

object CustomTypeBinders extends CustomTypeBinders
