package com.tiqwab.example.sqs

object SendMessageMain {

  def main(args: Array[String]): Unit = {
    val sender = new SimpleMessageSender(
      queueUrl =
        "https://sqs.ap-northeast-1.amazonaws.com/517530801117/sample-queue"
    )
    sender.send("visibility_timeout_sample")
  }

}
