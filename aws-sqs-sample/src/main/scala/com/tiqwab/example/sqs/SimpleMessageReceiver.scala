package com.tiqwab.example.sqs

import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.amazonaws.services.sqs.model.{
  ChangeMessageVisibilityRequest,
  Message,
  ReceiveMessageRequest
}

import scala.collection.JavaConverters._

class SimpleMessageReceiver(
    val queueUrl: String
) {

  lazy val sqs: AmazonSQS = AmazonSQSClientBuilder.defaultClient()

  def receive(f: Message => Unit, doesDelete: Boolean = true): Unit = {
    val request = new ReceiveMessageRequest(queueUrl)
      .withAttributeNames("ApproximateReceiveCount")
    val messages = sqs.receiveMessage(request).getMessages.asScala
    messages.foreach { m =>
      f(m)
      m.getAttributes
        .get("ApproximateReceiveCount")
        .asInstanceOf[String]
        .toInt match {
        case 1 =>
          sqs.changeMessageVisibility(
            createChangeVisibilityRequest(10, m.getReceiptHandle))
        case 2 =>
          sqs.changeMessageVisibility(
            createChangeVisibilityRequest(20, m.getReceiptHandle))
        case 3 =>
          sqs.changeMessageVisibility(
            createChangeVisibilityRequest(120, m.getReceiptHandle))
          Thread.sleep(30000)
          sqs.changeMessageVisibility(
            createChangeVisibilityRequest(60, m.getReceiptHandle))
        case _ =>
          ()
      }
      if (doesDelete) sqs.deleteMessage(queueUrl, m.getReceiptHandle)
    }
  }

  def createChangeVisibilityRequest(
      timeout: Int,
      receiptHandle: String
  ): ChangeMessageVisibilityRequest =
    new ChangeMessageVisibilityRequest()
      .withQueueUrl(queueUrl)
      .withReceiptHandle(receiptHandle)
      .withVisibilityTimeout(timeout)

}
