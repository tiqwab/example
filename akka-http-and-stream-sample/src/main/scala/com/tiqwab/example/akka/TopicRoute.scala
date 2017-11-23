package com.tiqwab.example.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

class TopicRoute(val system: ActorSystem, val timeout: Timeout) extends TopicApi with LazyLogging {

  def routes: Route =
    pathPrefix("topic" / Segment) { topicName =>
      post {
        entity(as[SaveMessageRequest]) { request =>
          onComplete(saveMessage(topicName, request)) {
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
