package com.tiqwab.example.akka.lifegame

sealed trait CellStatus

object CellStatus {
  case object Alive extends CellStatus
  case object Dead extends CellStatus
}
