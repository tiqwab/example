package com.tiqwab.example.messaging.redispubsub

import akka.stream._
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler}
import org.slf4j.LoggerFactory

class PublisherSink(
    val channelName: String
) extends GraphStage[SinkShape[Message]] {

  val logger = LoggerFactory.getLogger(this.getClass)
  val in: Inlet[Message] = Inlet("SubscribeSink.In")
  val channel = Channel(channelName)

  override def shape: SinkShape[Message] = SinkShape(in)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      override def preStart(): Unit = {
        pull(in)
      }

      setHandler(
        in,
        new InHandler {
          override def onPush(): Unit = {
            val message = grab(in)
            logger.debug(s"will publish message: $message")
            RedisClient.publish(message, channel)
            pull(in)
          }
        }
      )
    }

}
