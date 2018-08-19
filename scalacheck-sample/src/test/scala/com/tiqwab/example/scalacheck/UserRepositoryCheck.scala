package com.tiqwab.example.scalacheck

import org.scalacheck._
import org.scalacheck.commands.Commands
import scalikejdbc.config._

import scala.util.Try

object UserRepositoryCheck extends Commands {

  override type State = Map[Long, User]

  override type Sut = UserRepositoryOnJdbc

  override def canCreateNewSut(newState: State,
                               initSuts: Traversable[State],
                               runningSuts: Traversable[Sut]): Boolean = {
    println("---")
    true
  }

  override def newSut(state: State): Sut =
    new UserRepositoryOnJdbc()

  override def destroySut(sut: Sut): Unit = ()

  override def initialPreCondition(state: State): Boolean = state.isEmpty

  override def genInitialState: Gen[State] = Gen.const(Map.empty[Long, User])

  // --- Gen

  def idGen: Gen[Long] = Gen.choose(1, Long.MaxValue)

  def nameGen: Gen[String] =
    for {
      len <- Gen.choose(1, 255)
      chars <- Gen.listOfN(len, Gen.alphaNumChar)
    } yield chars.mkString

  def userGen: Gen[User] =
    for {
      id <- idGen
      name <- nameGen
      age <- Gen.choose(1, 255)
    } yield User(id, name, age)

  def insertWithNewUserCmdGen(state: State): Gen[InsertWithNewUser] =
    userGen.filterNot(u => state.contains(u.id)).map(InsertWithNewUser.apply)
  def insertWithExistingUserCmdGen(state: State): Gen[InsertWithExistingUser] =
    Gen.oneOf(state.values.toSeq).map(InsertWithExistingUser.apply)
  def findByExistingIdCmdGen(state: State): Gen[FindByExistingId] =
    Gen.oneOf(state.keys.toSeq).map(FindByExistingId.apply)
  def findByUnknownIdCmdGen(state: State): Gen[FindByUnknownId] =
    idGen.filterNot(state.contains).map(FindByUnknownId.apply)
  def updateWithExistingUserCmdGen(state: State): Gen[UpdateWithExistingUser] =
    for {
      user <- userGen
      id <- Gen.oneOf(state.keys.toSeq)
    } yield UpdateWithExistingUser(user.copy(id = id))
  def updateWithUnknownUserCmdGen(state: State): Gen[UpdateWithUnknownUser] =
    userGen.filterNot(u => state.contains(u.id)).map(UpdateWithUnknownUser.apply)
  def deleteByExistingIdCmdGen(state: State): Gen[DeleteByExistingId] =
    Gen.oneOf(state.keys.toSeq).map(DeleteByExistingId.apply)
  def deleteByUnknownIdCmdGen(state: State): Gen[DeleteByUnknownId] =
    idGen.filterNot(state.contains).map(DeleteByUnknownId.apply)

  override def genCommand(state: State): Gen[UserRepositoryCheck.Command] =
    if (state.isEmpty) {
      Gen.oneOf(insertWithNewUserCmdGen(state),
                findByUnknownIdCmdGen(state),
                updateWithUnknownUserCmdGen(state),
                deleteByUnknownIdCmdGen(state))
    } else {
      Gen.oneOf(
        insertWithNewUserCmdGen(state),
        insertWithExistingUserCmdGen(state),
        findByExistingIdCmdGen(state),
        findByUnknownIdCmdGen(state),
        updateWithExistingUserCmdGen(state),
        updateWithUnknownUserCmdGen(state),
        deleteByExistingIdCmdGen(state),
        deleteByUnknownIdCmdGen(state)
      )
    }

  // --- Command
  case class InsertWithNewUser(user: User) extends SuccessCommand {

    override type Result = Try[Unit]

    override def preCondition(state: Map[Long, User]): Boolean = !state.contains(user.id)

    override def run(sut: UserRepositoryOnJdbc): Result = { println(this); sut.insert(user) }

    override def postCondition(state: Map[Long, User], result: Try[Unit]): Prop =
      // postCondition cannot see the result of nextState
      result.isSuccess // && state.contains(user.id)

    override def nextState(state: Map[Long, User]): Map[Long, User] = state + (user.id -> user)

  }

  case class InsertWithExistingUser(user: User) extends SuccessCommand {

    override type Result = Try[Unit]

    override def preCondition(state: Map[Long, User]): Boolean = state.contains(user.id)

    override def run(sut: UserRepositoryOnJdbc): Result = { println(this); sut.insert(user) }

    override def postCondition(state: Map[Long, User], result: Try[Unit]): Prop = {
      val resultCheck = result.fold(
        {
          case _: UserAlreadyExistsException =>
            Prop.passed
          case e =>
            Prop.falsified :| s"expect UserAlredayExistsException, but $e"
        }, { _ =>
          Prop.falsified :| "expect UserAlreadyExistsException"
        }
      )
      resultCheck && state.contains(user.id)
    }

    override def nextState(state: Map[Long, User]): Map[Long, User] = state

  }

  case class FindByExistingId(id: Long) extends SuccessCommand {

    override type Result = Try[Option[User]]

    override def preCondition(state: Map[Long, User]): Boolean = state.contains(id)

    override def run(sut: UserRepositoryOnJdbc): Try[Option[User]] = { println(this); sut.findById(id) }

    override def nextState(state: Map[Long, User]): Map[Long, User] = state

    override def postCondition(state: Map[Long, User], result: Try[Option[User]]): Prop =
      result.isSuccess && result.get.isDefined

  }

  case class FindByUnknownId(id: Long) extends SuccessCommand {

    override type Result = Try[Option[User]]

    override def preCondition(state: Map[Long, User]): Boolean = !state.contains(id)

    override def run(sut: UserRepositoryOnJdbc): Try[Option[User]] = { println(this); sut.findById(id) }

    override def nextState(state: Map[Long, User]): Map[Long, User] = state

    override def postCondition(state: Map[Long, User], result: Try[Option[User]]): Prop =
      result.isSuccess && result.get.isEmpty

  }

  case class UpdateWithExistingUser(user: User) extends SuccessCommand {

    override type Result = Try[Unit]

    override def preCondition(state: Map[Long, User]): Boolean = state.contains(user.id)

    override def run(sut: UserRepositoryOnJdbc): Try[Unit] = { println(this); sut.update(user) }

    override def postCondition(state: Map[Long, User], result: Try[Unit]): Prop = result.isSuccess

    override def nextState(state: Map[Long, User]): Map[Long, User] = state.updated(user.id, user)

  }

  case class UpdateWithUnknownUser(user: User) extends SuccessCommand {

    override type Result = Try[Unit]

    override def preCondition(state: Map[Long, User]): Boolean = !state.contains(user.id)

    override def run(sut: UserRepositoryOnJdbc): Try[Unit] = { println(this); sut.update(user) }

    override def postCondition(state: Map[Long, User], result: Try[Unit]): Prop =
      result.fold(
        {
          case _: UserNotFoundException =>
            Prop.passed
          case e =>
            Prop.falsified :| s"expect UserNotFoundException, but $e"
        },
        _ => Prop.falsified :| "expect UserNotFoundException"
      )

    override def nextState(state: Map[Long, User]): Map[Long, User] = state

  }

  case class DeleteByExistingId(id: Long) extends SuccessCommand {

    override type Result = Try[Unit]

    override def preCondition(state: Map[Long, User]): Boolean = state.contains(id)

    override def run(sut: UserRepositoryOnJdbc): Try[Unit] = { println(this); sut.deleteById(id) }

    override def postCondition(state: Map[Long, User], result: Try[Unit]): Prop = result.isSuccess

    override def nextState(state: Map[Long, User]): Map[Long, User] = state - id

  }

  case class DeleteByUnknownId(id: Long) extends SuccessCommand {

    override type Result = Try[Unit]

    override def preCondition(state: Map[Long, User]): Boolean = !state.contains(id)

    override def run(sut: UserRepositoryOnJdbc): Try[Unit] = { println(this); sut.deleteById(id) }

    override def postCondition(state: Map[Long, User], result: Try[Unit]): Prop =
      result.fold(
        {
          case _: UserNotFoundException =>
            Prop.passed
          case e =>
            Prop.falsified :| s"expect UserNotFoundException, but $e"
        },
        _ => Prop.falsified :| "expect UserNotFoundException"
      )

    override def nextState(state: Map[Long, User]): Map[Long, User] = state

  }

  // --- main

  /*
  def main(args: Array[String]): Unit = {
    DBs.setupAll()
    UserRepositoryCheck.property().check()
  }
 */

}

class UserRepositoryCheck extends Properties("UserRepository") {

  property("stateful test") = {
    DBs.setupAll()
    UserRepositoryCheck.property()
  }

}
