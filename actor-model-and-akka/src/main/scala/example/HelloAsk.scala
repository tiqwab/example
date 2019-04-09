package example

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object HelloAsk {
  def main(args: Array[String]): Unit = {
    import EchoActor._
    val system = ActorSystem("hello")
    implicit val ec: ExecutionContext = system.dispatcher
    implicit val timeout: Timeout = Timeout(5.seconds)
    val actorRef = system.actorOf(EchoActor.props)
    val fut: Future[String] = (actorRef ? EchoMessage("Hello World!")).mapTo[String]
    // actorRef.ask(Greeting("Hello World!"))
    fut.onComplete {
      case Success(msg) => println(msg); system.terminate()
      case Failure(e)   => println(e.getMessage); system.terminate()
    }
  }

  class EchoActor extends Actor {
    import EchoActor._
    override def receive: Receive = {
      case EchoMessage(msg) => sender ! msg
    }
  }

  object EchoActor {
    case class EchoMessage(msg: String)
    def props: Props = Props[EchoActor]
  }
}
