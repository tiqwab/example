package com.tiqwab.example.akka

import akka.actor._

class TopicList extends Actor with ActorLogging {

  var topicMap: Map[String, ActorRef] = Map.empty[String, ActorRef]

  override def receive: Receive = {
    case TopicList.SaveMessage(topicName, body, timestampMillis) =>
      val topic = topicMap.getOrElse(topicName, {
        val newTopic = context.actorOf(Topic.props(topicName))
        topicMap = topicMap + (topicName -> newTopic)
        newTopic
      })
      topic forward Topic.SaveMessage(body, timestampMillis)
    case TopicList.GetMessage(topicName, id) =>
      topicMap.get(topicName) match {
        case None =>
          sender() ! None
        case Some(topic) =>
          topic forward Topic.GetMessage(id)
      }
    case TopicList.ListMessage(topicName) =>
      topicMap.get(topicName) match {
        case None =>
          sender() ! None
        case Some(topic) =>
          topic forward Topic.ListMessage
      }
  }

}

object TopicList {
  def props: Props = Props(new TopicList())
  case class SaveMessage(topic: String, body: String, timestampMillis: Long)
  case class MessageSaved(id: String)
  case class GetMessage(topic: String, id: String)
  case class ListMessage(topic: String)
}
