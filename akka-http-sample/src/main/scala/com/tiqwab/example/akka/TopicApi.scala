package com.tiqwab.example.akka

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future

trait TopicApi {

  implicit val system: ActorSystem
  implicit val timeout: Timeout

  val topicList: ActorRef = system.actorOf(TopicList.props, "topicList")

  def saveMessage(topicName: String, request: SaveMessageRequest): Future[Message] =
    (topicList ? TopicList.SaveMessage(topicName, request.body, request.timestampMillis)).mapTo[Message]

  def getMessage(topic: String, id: String): Future[Option[Message]] =
    (topicList ? TopicList.GetMessage(topic, id)).mapTo[Option[Message]]

  def listMessage(topic: String): Future[Option[Seq[Message]]] =
    (topicList ? TopicList.ListMessage(topic)).mapTo[Option[Seq[Message]]]

}
