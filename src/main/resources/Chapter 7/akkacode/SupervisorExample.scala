package akkacode

import akka.actor.Props
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._

object SupervisorExample extends App {
  case object Throw
  case object SimpleMessage
  
  class ParentActor extends Actor {
    override def preStart() = {
      context.actorOf(Props[ChildActor], "child1")
      context.actorOf(Props[ChildActor], "child2")
      context.actorOf(Props[ChildActor], "child3")
    }
    def receive = {
      case m =>
        println("Parent")
        context.children.foreach(_ ! SimpleMessage)
    }
    override val supervisorStrategy = OneForOneStrategy(loggingEnabled = false) {
      case ex:Exception =>
        println("Child had an exception")
        Restart
    }
  }
  
  class ChildActor extends Actor {
    println("Making a child "+self.path)
    def receive = {
      case Throw => 
        println("Child Dying")
        throw new Exception("Something bad happened.")
      case SimpleMessage =>
        println("Child Simple = "+self.path)
    }
    override def preStart() = {
      super.preStart
      println("Prestart")
    }
    override def postStop() = {
      super.postStop
      println("Poststop")
    }
    override def preRestart(reason:Throwable, message: Option[Any]) = {
      super.preRestart(reason, message)
      println("Prerestart "+message)
    }
    override def postRestart(reason:Throwable) = {
      super.postRestart(reason)
      println("Postrestart")
    }
  }
  
  val system = ActorSystem("SimpleExample")
  val actor1 = system.actorOf(Props[ParentActor],"parent1")
  
  val c1 = system.actorSelection("akka://SimpleExample/user/parent1/child1")
  c1 ! Throw
//  Thread.sleep(10)
  c1 ! SimpleMessage
  
  system.shutdown

}