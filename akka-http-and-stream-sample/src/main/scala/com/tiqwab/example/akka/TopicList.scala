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
  }

}

object TopicList {
  def props: Props = Props(new TopicList())
  case class SaveMessage(topic: String, body: String, timestampMillis: Long)
  case class MessageSaved(id: String)
}
