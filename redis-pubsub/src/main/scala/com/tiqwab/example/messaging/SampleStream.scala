package com.tiqwab.example.messaging

import scala.concurrent.ExecutionContext.Implicits._

import akka.stream._
import akka.stream.scaladsl._

import akka.actor.ActorSystem

object SampleStream {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("sample-stream")
    implicit val materializer = ActorMaterializer()

    Source(List(1, 2, 3, 4, 5))
      .map(_ * 2)
      .runWith(Sink.foreach(println(_)))
      .onComplete(_ => system.terminate())
  }
}
