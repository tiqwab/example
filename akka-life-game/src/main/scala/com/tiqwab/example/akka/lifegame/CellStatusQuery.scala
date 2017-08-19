package com.tiqwab.example.akka.lifegame

import akka.actor.{Actor, ActorRef, Props}

/**
  * 現在の Cell の状態を集める
  * @param requester
  * @param requestId
  * @param cellRefs
  */
class CellStatusQuery(
    val requester: ActorRef,
    val requestId: Long,
    val cellRefs: Map[Position, ActorRef]
) extends Actor {
  import CellStatusQuery._

  val statuses = scala.collection.mutable.Map.empty[Position, CellStatus]

  override def preStart(): Unit = {
    cellRefs.values foreach { cell =>
      cell ! RequestCellStatus
    }
  }

  override def receive: Receive = {
    case ResponseCellStatus(position, status) =>
      statuses += (position -> status)
      if (statuses.size == cellRefs.size) {
        requester ! ResponseCellStatuses(requestId, statuses.toMap)
        context.stop(self)
      }
  }
}

object CellStatusQuery {

  def props(requester: ActorRef,
            requestId: Long,
            cellRefs: Map[Position, ActorRef]): Props =
    Props(new CellStatusQuery(requester, requestId, cellRefs))

  case object RequestCellStatus
  case class ResponseCellStatus(position: Position, status: CellStatus)
  case class ResponseCellStatuses(requestId: Long,
                                  statuses: Map[Position, CellStatus])

}
