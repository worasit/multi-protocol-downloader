package akkacode

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef

object CountDown extends App {
  case class StartCounting(n: Int, partner: ActorRef)
  case class Count(n:Int)
  
  class CountDownActor extends Actor {
    def receive = {
      case StartCounting(n,o) => 
        println(n)
        o ! Count(n-1)
      case Count(n) =>
        if(n>0) {
          println(n)
          Thread.sleep(1000)
          sender ! Count(n-1)
        } else {
          context.system.shutdown
        }
    }
  }

  val system = ActorSystem("SimpleExample")
  val actor1 = system.actorOf(Props[CountDownActor], "Actor1")
  val actor2 = system.actorOf(Props[CountDownActor], "Actor2")

  actor1 ! StartCounting(10, actor2)

//  system.shutdown
}