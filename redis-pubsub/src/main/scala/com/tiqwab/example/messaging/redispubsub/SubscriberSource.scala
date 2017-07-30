package com.tiqwab.example.messaging.redispubsub

import java.util.concurrent.CyclicBarrier

import akka.stream._
import akka.stream.scaladsl._
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import org.slf4j.LoggerFactory

class SubscriberSource(
    val channelName: String
) extends GraphStage[SourceShape[Message]] {

  val logger = LoggerFactory.getLogger(this.getClass())
  val out: Outlet[Message] = Outlet("SubscriberSource.Out")
  val channel = Channel(channelName)

  override def shape: SourceShape[Message] = new SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      private val barrier = new CyclicBarrier(2)
      private var message: Option[Message] = None
      private var threadName: Option[String] = None

      private val messageReceiver = new MessageReceiver {
        override def handleMessage(m: Message): Unit = {
          message = Some(m.copy())
          barrier.await()
        }
      }

      override def preStart(): Unit = {
        super.preStart()
        threadName = Some(RedisClient.subscribe(channel, messageReceiver))
      }

      override def postStop(): Unit = {
        super.postStop()
        RedisClient.unsubscribe(threadName.get)
      }

      setHandler(
        out,
        new OutHandler() {
          override def onPull(): Unit = {
            barrier.await()
            logger.debug(s"subscribed message: ${message.get}")
            push(out, message.get)
            message = None
            barrier.reset()
          }
        }
      )
    }

}
