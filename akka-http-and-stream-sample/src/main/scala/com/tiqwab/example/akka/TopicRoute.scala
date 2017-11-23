package com.tiqwab.example.akka

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.util.{Failure, Success}

class TopicRoute extends TopicApi with LazyLogging {

  def routes: Route =
    pathPrefix("topic" / Segment) { topicName =>
      post {
        entity(as[SaveMessageRequest]) { request =>
          onComplete(saveMessage(request)) {
            case Success(message) =>
              complete((OK, SaveMessageResponse(message.id)))
            case Failure(e) =>
              logger.error("Error occurred while saving message", e)
              complete(InternalServerError)
          }
        }
      }
    }

}

trait TopicApi {

  def saveMessage(request: SaveMessageRequest): Future[Message] =
    Future.successful(
      Message(
        scala.util.Random.nextInt(100).toString,
        request.body,
        request.timestampMillis
      )
    )

  def getMessage(topic: String, id: String): Future[Option[Message]] = ???

  def listMessage(topic: String): Future[Seq[Message]] = ???

}
