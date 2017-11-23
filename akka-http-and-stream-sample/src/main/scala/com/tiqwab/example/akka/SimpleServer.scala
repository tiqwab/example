package com.tiqwab.example.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object SimpleServer extends LazyLogging {

  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load()
    val host = config.getString("http.host")
    val port = config.getInt("http.port")
    val timeout = config.getInt("http.timeout").seconds

    implicit val system: ActorSystem = ActorSystem("simple-system", config)
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    val route = new TopicRoute(system, Timeout(timeout)).routes
    Http().bindAndHandle(route, host, port).onComplete {
      case Success(binding) =>
        logger.info(s"Server start at ${binding.localAddress}")
      case Failure(e) =>
        logger.error(s"Error occurred while starting server", e)
        Await.result(system.terminate(), Duration.Inf)
        sys.exit(1)
    }
  }

}
