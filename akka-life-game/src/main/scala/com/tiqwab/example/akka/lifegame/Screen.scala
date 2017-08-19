package com.tiqwab.example.akka.lifegame

import akka.actor.{Actor, Props}
import scala.concurrent.duration._

/**
  * LifeGame を画面に出力する
  * @param size
  */
// Actor を継承し、receive メソッドを実装する
class Screen(val size: Int) extends Actor {
  import Screen._
  // Actor 内部で使用するための ExecutionContext を提供する
  // ref. http://doc.akka.io/docs/akka/current/scala/futures.html
  import context.dispatcher

  // context.actorOf で自身の子供となる Actor を作成する
  // Actor は 1 つの親と 0 以上の子を持ち全体としては木構造になる
  val board = context.actorOf(Board.props(size))

  // periodic に実行したい処理等には scheduler が便利
  val updateTimer = context.system.scheduler.schedule(
    1.second,
    1.second,
    self,
    UpdateScreen
  )

  // Actor 内に可変な field を持たせることができる
  // receive が同時に呼び出されることはないので、field に基づくクラスの不変条件等を守りやすい
  // 逆にいうと内部の情報をそのままメッセージとして外に送るべきではない
  var requestCounter = 1

  // この Actor で行いたい処理は receive で定義する
  // type Receive = PartialFunction[Any, Unit]
  override def receive: Receive = {
    case UpdateScreen =>
      // ! は `tell` の alias で対象の Actor にメッセージを送信する
      board ! Board.UpdateCellStatusesRequest(requestCounter)
      requestCounter += 1
    case Board.UpdateCellStatusesResponse(requestId, statuses) =>
      statuses.toSeq
        .sortBy { case ((x, y), _) => (y, x) }
        .grouped(size)
        .foreach { row =>
          println(
            row
              .map {
                case (_, CellStatus.Alive) => "1"
                case (_, CellStatus.Dead)  => "0"
              }
              .mkString(" ")
          )
        }
      println()
  }

}

object Screen {

  // Actor 作成時に使用する Props をコンパニオンオブジェクトで定義するのがおすすめ
  // ref. http://doc.akk  a.io/docs/akka/current/scala/actors.html#props
  def props(size: Int): Props = Props(new Screen(size))

  // Actor が送受信するメッセージはコンパニオンオブジェクトで定義するとわかりやすそう
  case object UpdateScreen

}
