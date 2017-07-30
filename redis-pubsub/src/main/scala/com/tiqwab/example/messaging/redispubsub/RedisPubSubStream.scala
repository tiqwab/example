package com.tiqwab.example.messaging.redispubsub

import java.time.ZonedDateTime
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

object RedisPubSubStream {

  val logger = LoggerFactory.getLogger(this.getClass())

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("pubsub")
    implicit val materializer = ActorMaterializer()

    val channelName = "channel1"

    val publisherSink = new PublisherSink(channelName)
    Source
      .tick(1.seconds, 5.seconds, Message(s"Hello, "))
      .map(m => {
        m.copy(data = m.data + s"this is ${ZonedDateTime.now()}")
      })
      .runWith(Sink.fromGraph(publisherSink))

    val subscriberSource = new SubscriberSource(channelName)
    Source
      .fromGraph(subscriberSource)
      .runWith(Sink.foreach(x => logger.debug(x.toString)))
      .onComplete(_ => system.terminate())
  }

}
