package com.tiqwab.example.sqs

import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.amazonaws.services.sqs.model.{
  ChangeMessageVisibilityRequest,
  Message,
  ReceiveMessageRequest
}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration._
import scala.collection.JavaConverters._

class ExtensionMessageReceiver(
    val queueUrl: String,
    val visibilityTimeout: Duration,
    val sleepDuration: Duration
) {

  lazy val logger: Logger = LoggerFactory.getLogger(getClass)
  lazy val sqs: AmazonSQS = AmazonSQSClientBuilder.defaultClient()

  def receive(): Unit = {
    val request = new ReceiveMessageRequest(queueUrl)
      .withAttributeNames("ApproximateReceiveCount")
    val messages = sqs.receiveMessage(request).getMessages.asScala
    messages.foreach { m =>
      logger.debug(s"get message: ${m.getBody}")
      while (true) {
        logger.debug(
          s"Set visibility timeout to ${visibilityTimeout.toSeconds}")
        sqs.changeMessageVisibility(
          createChangeVisibilityRequest(visibilityTimeout.toSeconds.toInt,
                                        m.getReceiptHandle))
        logger.debug(s"sleep for ${sleepDuration.toSeconds} seconds")
        Thread.sleep(sleepDuration.toMillis)
      }
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
