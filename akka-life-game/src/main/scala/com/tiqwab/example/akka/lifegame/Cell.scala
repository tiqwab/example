package com.tiqwab.example.akka.lifegame

import akka.actor.{Actor, Props}

class Cell(
    val initialPosition: Position,
    val initialStatus: CellStatus
) extends Actor {
  import Cell._

  private var status: CellStatus = initialStatus
  private val position: Position = initialPosition

  override def receive: Receive = {
    case ChangeToAlive((_, _)) =>
      status = CellStatus.Alive
    case ChangeToDead((_, _)) =>
      status = CellStatus.Dead
    case CellStatusQuery.RequestCellStatus =>
      sender() ! CellStatusQuery.ResponseCellStatus(position, status)
  }

}

object Cell {

  case class ChangeToAlive(position: Position)
  case class ChangeToDead(position: Position)

  def props(position: Position, status: CellStatus): Props =
    Props(new Cell(position, status))

  /**
    * 現在の自身と周囲の status から次の状態を変更すべきか判断する。
    * @param status 自身の CellStatus
    * @param neighbors 周囲の CellStatus
    * @return 変更する場合、その status, 変更しない場合、None
    */
  def willChangeState(status: CellStatus,
                      neighbors: Seq[CellStatus]): Option[CellStatus] = {
    val aliveCount = neighbors.count(_ == CellStatus.Alive)
    status match {
      case CellStatus.Alive =>
        if (aliveCount <= 1 || aliveCount >= 4) {
          Some(CellStatus.Dead)
        } else {
          None
        }
      case CellStatus.Dead =>
        if (aliveCount == 2 || aliveCount == 3) {
          Some(CellStatus.Alive)
        } else {
          None
        }
    }
  }

}
