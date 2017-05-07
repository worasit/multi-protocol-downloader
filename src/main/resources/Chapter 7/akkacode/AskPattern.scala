package akkacode

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.Patterns._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

object AskPattern extends App {
  case object AskName
  
  class AskActor(val name:String) extends Actor {
    def receive = {
      case AskName =>
        sender ! name
    }
  }
  
  val system = ActorSystem("SimpleExample")
  val actor = system.actorOf(Props(new AskActor("Bob")),"NamedActor")
  
  val nameFuture = ask(actor,AskName,1000.millis)

//  import ExecutionContext.Implicits.global
  implicit val ec = system.dispatcher

  nameFuture.onSuccess {
    case s => 
      println("name = "+s)
      system.shutdown
  }
//  println(Await.result(nameFuture,1000.millis))
//  system.shutdown
  
}