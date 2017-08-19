package com.tiqwab.example.akka.lifegame

import akka.actor.ActorSystem

object Main extends App {
  // Actor を動かすための ActorSystem を用意する
  val system = ActorSystem("life-game")
  // println(system) // akka://life-game

  // ユーザが作成する root Actor は ActorSystem から作成する
  // Actor はそれぞれ path で参照することができ、この場合 'akka://life-game/user/xxx' のようになる
  // ref. http://doc.akka.io/docs/akka/current/scala/general/addressing.html
  val screen = system.actorOf(Screen.props(size = 10))
  // println(screen) // Actor[akka://life-game/user/$a#1952565570]
}
