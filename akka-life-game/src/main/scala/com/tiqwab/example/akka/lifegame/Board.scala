package com.tiqwab.example.akka.lifegame

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.annotation.tailrec

/**
  * 0 以上の Cell を管理し、Screen と Cell の間の橋渡しをする
  * @param size
  */
class Board(val size: Int) extends Actor with ActorLogging {
  import Board._

  private val positionToCellMap =
    scala.collection.mutable.Map.empty[Position, ActorRef]
  private val cellToPositionMap =
    scala.collection.mutable.Map.empty[ActorRef, Position]

  // Actor 作成後 start する前に行いたい処理を書くことができる
  override def preStart(): Unit = {
    0 to (size - 1) foreach { y =>
      0 to (size - 1) foreach { x =>
        val status =
          if (scala.math.random < 0.5) CellStatus.Alive
          else CellStatus.Dead
        val cell = context.actorOf(Cell.props((x, y), status))
        positionToCellMap += ((x, y) -> cell)
        cellToPositionMap += (cell -> (x, y))
      }
    }
  }

  override def receive: Receive = {
    case UpdateCellStatusesRequest(requestId) =>
      // 全ての Cell の状態を集める作業は別の Actor を作成しそちらに任せる
      // 集め終わったときに下のメッセージを通して教えてもらう
      context.actorOf(
        CellStatusQuery.props(self, requestId, positionToCellMap.toMap))
    case CellStatusQuery.ResponseCellStatuses(requestId, statuses) =>
      // Cell の現在の状態から次に取るべき状態を計算し、Screen と Cell に通知する
      val UpdateCellPlan(toAlive, toDead) = createUpdateCellPlan(statuses)
      toAlive foreach { cell =>
        cell ! Cell.ChangeToAlive(cellToPositionMap(cell))
      }
      toDead foreach { cell =>
        cell ! Cell.ChangeToDead(cellToPositionMap(cell))
      }
      val updatedStatuses = {
        val u1 = toAlive.foldLeft(statuses)((s, cell) =>
          s.updated(cellToPositionMap(cell), CellStatus.Alive))
        toDead.foldLeft(u1)((s, cell) =>
          s.updated(cellToPositionMap(cell), CellStatus.Dead))
      }
      context.parent ! UpdateCellStatusesResponse(requestId, updatedStatuses)
  }

  /**
    * 現在の状態から死ぬべき、復活すべき Cell を計算する
    * @param statuses
    * @return
    */
  def createUpdateCellPlan(
      statuses: Map[Position, CellStatus]
  ): UpdateCellPlan = {
    @tailrec
    def loop(statusList: List[(Position, CellStatus)],
             toAlives: List[ActorRef],
             toDeads: List[ActorRef]): UpdateCellPlan = statusList match {
      case Nil =>
        UpdateCellPlan(toAlives, toDeads)
      case ((p @ (x, y), status) :: ss) =>
        val neighbors = Seq(
          (x - 1, y),
          (x - 1, y - 1),
          (x, y - 1),
          (x + 1, y - 1),
          (x + 1, y),
          (x + 1, y + 1),
          (x, y + 1),
          (x - 1, y + 1)
        ).map(statuses.get(_))
          .collect { case Some(x) => x }
        positionToCellMap.get(p) match {
          case None =>
            log.warning(s"Not found actor at position: $p")
            UpdateCellPlan(toAlives, toDeads)
          case Some(cell) =>
            Cell.willChangeState(status, neighbors) match {
              case None =>
                loop(ss, toAlives, toDeads)
              case Some(CellStatus.Dead) =>
                loop(ss, toAlives, cell :: toDeads)
              case Some(CellStatus.Alive) =>
                loop(ss, cell :: toAlives, toDeads)
            }
        }
    }
    loop(statuses.toList, List(), List())
  }

}

case class UpdateCellPlan(toAlive: Seq[ActorRef], toDead: Seq[ActorRef])

object Board {

  case class UpdateCellStatusesRequest(requestId: Long)
  case class UpdateCellStatusesResponse(requestId: Long,
                                        statuses: Map[Position, CellStatus])

  def props(size: Int): Props = Props(new Board(size))

}
