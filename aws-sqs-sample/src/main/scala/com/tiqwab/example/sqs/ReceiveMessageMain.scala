package com.tiqwab.example.sqs

import com.amazonaws.services.sqs.model.MessageAttributeValue
import scala.collection.JavaConverters._

import scala.annotation.tailrec

object ReceiveMessageMain {

  def main(args: Array[String]): Unit = {
    val receiver = new SimpleMessageReceiver(
      queueUrl =
        "https://sqs.ap-northeast-1.amazonaws.com/517530801117/sample-queue"
    )

    @tailrec
    def loop(n: Int): Unit = n match {
      case 0 =>
        ()
      case _ =>
        println(s"${System.currentTimeMillis()}: ")
        receiver.receive(
          msg => {
            println(s"body: ${msg.getBody}, attributes: ${msg.getAttributes}")
          },
          false
        )
        Thread.sleep(1000)
        loop(n - 1)
    }
    loop(180)
  }
}
