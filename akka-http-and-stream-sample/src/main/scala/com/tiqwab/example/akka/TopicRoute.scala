package com.tiqwab.example.akka

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route

class TopicRoute {

  def routes: Route =
    pathPrefix("topic" / Segment) { topicName =>
      complete((OK, s"Accept request for $topicName"))
    }

}
