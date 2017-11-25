package com.tiqwab.example.akka

import akka.actor._

class Topic(val topicName: String) extends Actor with ActorLogging {
  var nextId: Long = 1
  var messages: Seq[Message] = Seq.empty[Message]
  override def receive: Receive = {
    case Topic.SaveMessage(body, timestampMillis) =>
      val id = nextId.toString
      nextId += 1
      val message = Message(id, body, timestampMillis)
      messages = messages :+ message
      sender() ! message
    case Topic.GetMessage(id) =>
      sender() ! messages.find(_.id == id)
    case Topic.ListMessage =>
      // 単体で見ると Some で包む必要は無いが、TopicList からの使われ方の関係で
      sender() ! Some(messages)
  }
}

object Topic {
  def props(topicName: String): Props = Props(new Topic(topicName))
  case class SaveMessage(body: String, timestampMillis: Long)
  case class GetMessage(id: String)
  case object ListMessage
}
