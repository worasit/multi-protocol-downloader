package akkacode

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props

object HierarchyExample extends App {
  case object Cascade
  case object SimpleMessage
  
  class ParentActor extends Actor {
    override def preStart() = {
      context.actorOf(Props[ChildActor], "child1")
      context.actorOf(Props[ChildActor], "child2")
      context.actorOf(Props[ChildActor], "child3")
    }
    def receive = {
      case Cascade =>
        println("Parent")
        context.children.foreach(_ ! SimpleMessage)
    }
  }
  
  class ChildActor extends Actor {
    def receive = {
      case Cascade => 
        println("Child Cascade")
        context.parent ! Cascade
      case SimpleMessage =>
        println("Child Simple = "+self.path)
    }
  }
  
  val system = ActorSystem("HierarchyExample")
  val actor1 = system.actorOf(Props[ParentActor],"parent1")
  val actor2 = system.actorOf(Props[ParentActor],"parent2")
  
  val c1 = system.actorSelection("akka://HierarchyExample/user/parent1/child1")
  c1 ! Cascade
  val c2 = system.actorSelection("/user/parent2/child1")
  c2 ! Cascade
  
//  system.shutdown

}