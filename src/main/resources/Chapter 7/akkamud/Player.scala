package akkamud

import java.io.PrintStream
import akka.actor.ActorRef
import scala.concurrent.Await

object Player {
  case class Command(str: String)
  case class Print(message: String)
  case class SetRoom(room: ActorRef)
  case class SetName(name: String)
  case object PrintPrompt
}

class Player(out: PrintStream) extends akka.actor.Actor {
  import Player._
  private var name = ""
  var room: ActorRef = null

  def receive = {
    case SetName(n) => name = n
    case Command(s) =>
      if (s.isEmpty) {
        printPrompt
      } else {
        val (com, rest) = {
          val index = s.indexOf(" ")
          if (index < 0) (s, "") else s.splitAt(index)
        }
        if ("look".startsWith(com)) {
          doLook(rest)
        } else if ("north".startsWith(com)) {
          room ! Room.Exit("north", name, self)
        } else if ("south".startsWith(com)) {
          room ! Room.Exit("south", name, self)
        } else if ("east".startsWith(com)) {
          room ! Room.Exit("east", name, self)
        } else if ("west".startsWith(com)) {
          room ! Room.Exit("west", name, self)
        } else if ("up".startsWith(com)) {
          room ! Room.Exit("up", name, self)
        } else if ("down".startsWith(com)) {
          room ! Room.Exit("down", name, self)
        } else if ("say".startsWith(com)) {
          room ! Room.Say(rest.trim,name)
        } else if ("tell".startsWith(com)) {
          val (to, rest2) = {
            val index = rest.trim.indexOf(" ")
            if (index < 0) ("","") else rest.trim.splitAt(index)
          }
          if(to.isEmpty) out.println("Who did you want to tell what?") else room ! Room.Tell(rest2.trim,name,to)
        } else {
          out.println("Unknown command.")
          printPrompt
        }
      }
    case Print(msg) =>
      out.println(msg)
    case PrintPrompt =>
      printPrompt
    case SetRoom(r) => room = r
  }

  def printPrompt: Unit = {
    out.print("> ")
  }
  
  def doLook(rest:String):Unit = {
    if(rest.isEmpty()) room ! Room.PrintDescription(PrintPrompt)
    else if(Set("north","south","east","west","up","down").contains(rest)) {
      // TODO
    } else {
      // TODO try items
    }
  }
}