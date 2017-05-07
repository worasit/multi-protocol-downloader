package akkacode

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor

object SimpleExample extends App {
  class SimpleActor extends Actor {
    def receive = {
      case s:String => println("String "+s)
      case i:Int => println("Int "+i)
//      case _ => println("Unknown message")
    }
    def foo() = {}
  }
  
  val system = ActorSystem("SimpleExample")
  val actor = system.actorOf(Props[SimpleActor],"FirstActor")
  
  actor ! "Hi"
  actor ! 42
  actor ! 'a'
  
  system.shutdown
}