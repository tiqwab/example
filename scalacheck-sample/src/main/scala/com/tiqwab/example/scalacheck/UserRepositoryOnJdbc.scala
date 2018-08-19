package com.tiqwab.example.scalacheck

import scalikejdbc._

import scala.util.Try

class UserRepositoryOnJdbc {

  def insert(user: User)(implicit session: DBSession = AutoSession): Try[Unit] =
    findById(user.id) map {
      case None =>
        sql"INSERT INTO USERS (id, name, age) VALUES (${user.id}, ${user.name}, ${user.age});".execute.apply()
      case Some(_) =>
        throw new UserAlreadyExistsException()
    }

  def findById(id: Long)(implicit session: DBSession = AutoSession): Try[Option[User]] = Try {
    sql"SELECT id, name, age FROM USERS WHERE id = $id"
      .map { rs =>
        User(rs.long("id"), rs.string("name"), rs.int("age"))
      }
      .single
      .apply()
  }

  def update(user: User)(implicit session: DBSession = AutoSession): Try[Unit] = Try {
    val updated = sql"UPDATE USERS SET name = ${user.name}, age = ${user.age} WHERE id = ${user.id}".update.apply()
    if (updated == 0) {
      throw new UserNotFoundException()
    }
    ()
  }

  def deleteById(id: Long)(implicit session: DBSession = AutoSession): Try[Unit] = Try {
    val updated = sql"DELETE FROM USERS WHERE id = $id".update.apply()
    if (updated == 0) {
      throw new UserNotFoundException()
    }
    ()
  }

}

class UserAlreadyExistsException extends Exception
class UserNotFoundException extends Exception
