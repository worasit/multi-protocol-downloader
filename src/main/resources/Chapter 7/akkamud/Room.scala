package akkamud

import akka.actor.Actor
import akka.actor.ActorRef
import collection.mutable

object Room {
  case class Arrive(pname:String,who:ActorRef)
  case class Exit(dir:String,pname:String,who:ActorRef)
  case class LinkRooms(keywordMap:Map[String,(String,ActorRef)])
  case class PrintDescription(response:Any)
  case class Say(message:String,name:String)
  case class Tell(message:String,name:String,to:String)
}

class Room(node:xml.Node) extends Actor {
  import Room._
  
  private val name = (node \ "@name").text
  private val keyword = (node \ "@keyword").text
  private val description = (node \ "description").text
  private val exitsToKeyword = (node \ "exit").groupBy(n => (n \ "@dir").text).map(t => t._1 -> (t._2 \ "@dest").text)
  private var exitsToActor:Map[String,(String,ActorRef)] = null
  // TODO: private val items
  private val players = mutable.Buffer[(String,ActorRef)]()
  
  def receive = {
    case Exit(dir,pname,who) =>
      if(exitsToActor.contains(dir)) {
        players.remove(players.indexWhere(_._2==who),1)
        for((_,p) <- players) p ! Player.Print(s"$pname has left to the $dir.")
        exitsToActor(dir)._2 ! Arrive(pname,who)
      } else who ! Player.Print("You can't go that way.")
    case Arrive(pname,who) =>
      who ! Player.Print(makeDescription)
      for((_,p) <- players) p ! Player.Print(pname+" has arrived.")
      who ! Player.SetRoom(self)
      players += pname -> who
      who ! Player.PrintPrompt
    case LinkRooms(kwm) =>
      exitsToActor = for((dir,key) <- exitsToKeyword) yield (dir -> kwm(key))
    case PrintDescription(response) =>
      sender ! Player.Print(makeDescription)
      sender ! response
    case Say(msg,name) =>
      for((_,p) <- players; if p!=sender) p ! Player.Print(s"$name says '$msg'")
      sender ! Player.PrintPrompt
    case Tell(msg,name,to) =>
      if(players.exists(_._1 == to)) for((n,p) <- players; if to==n) p ! Player.Print(s"$name told you '$msg'")
      else sender ! Player.Print("That person isn't here.")
      sender ! Player.PrintPrompt
  }
  
  def makeDescription:String = {
    name+"\n"+description+"\n"+
    exitsToActor.map(t => t._2._1+" is to the "+t._1).mkString("\n")+"\n"+
    players.map(t => t._1+" is here.").mkString("\n")
  }
}