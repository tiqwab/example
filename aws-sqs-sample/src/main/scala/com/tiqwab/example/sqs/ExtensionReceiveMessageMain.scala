package com.tiqwab.example.sqs

import scala.concurrent.duration._

object ExtensionReceiveMessageMain {

  def main(args: Array[String]): Unit = {
    val receiver = new ExtensionMessageReceiver(
      queueUrl =
        "https://sqs.ap-northeast-1.amazonaws.com/517530801117/sample-queue",
      visibilityTimeout = 60.minute,
      sleepDuration = 30.minutes
    )

    receiver.receive()
  }
}
