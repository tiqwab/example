package example

import akka.actor._

import scala.io.StdIn

object Hello {
  def main(args: Array[String]): Unit = {
    import HelloActor._
    val system = ActorSystem("hello")
    val actorRef = system.actorOf(HelloActor.props)
    actorRef ! Greeting("Hello World!")
    // actorRef.tell(Greeting("Hello World!"), Actor.noSender)

    println("press enter to exit")
    StdIn.readLine()
    system.terminate()
  }

  class HelloActor extends Actor {
    import HelloActor._
    override def receive: Receive = {
      case Greeting(msg) => println(msg)
    }
  }

  object HelloActor {
    sealed trait HelloMessage
    case class Greeting(msg: String) extends HelloMessage
    def props: Props = Props[HelloActor]
  }
}
