package com.tiqwab.example.sqs

import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.amazonaws.services.sqs.model.{
  MessageAttributeValue,
  SendMessageRequest
}
import scala.collection.JavaConverters._

class SimpleMessageSender(
    val queueUrl: String
) {

  lazy val sqs: AmazonSQS = AmazonSQSClientBuilder.defaultClient()

  def send(msg: String): Unit = {
    val request = new SendMessageRequest()
      .withMessageBody(msg)
      .withQueueUrl(queueUrl)

    sqs.sendMessage(request)
  }

}
