package mud.immutable

import scala.util.parsing.combinator.JavaTokenParsers

object Player extends JavaTokenParsers {
  def apply(name: String, room: String): Player = {
    new Player(name, Nil, room)
  }

  val commands = Vector("drop","get","inv","help","look","save","quit")
  
  def command: Parser[Command] =
    "get" ~ ident ^^ { case _ ~ item => Get(item) } |
    "drop" ~ ident ^^ { case _ ~ item => Drop(item) } |
    """look\s+at""".r ~ ident ^^ { case _ ~ item => LookObject(item)} |
    """look\s+(to(ward)?(\s+the)?)?""".r ~ ("east" | "west" | "north" | "south" | "up" | "down") ^^ { case _ ~ dir => LookDir(dir)} |
    "look" ^^ { case _ => LookRoom } |
    "inv" ^^ { case _ => Inv } |
    "help" ^^ { case _ => Help } |
    "save" ^^ { case _ => Save }

  sealed trait Command extends ((Player, Map[String, Room]) => (Player, Map[String, Room])) {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room])
  }
  
  case class Get(item:String) extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      rooms(p.currentRoom).getItem(item).map(t => {
        val (item, room) = t
        (p.copy(items = item :: p.items), rooms + (p.currentRoom -> room))
      }) getOrElse {
        println("Item not found.")
        (p, rooms)
      }
    }
  }

  case class Drop(item:String) extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      p.items.find(_.matches(item)).map(item => {
        (p.copy(items = p.items.diff(List(item))), rooms + (p.currentRoom -> rooms(p.currentRoom).dropItem(item)))
      }) getOrElse {
        println("You don't have that item to drop.")
        (p, rooms)
      }
    }
  }

  case object LookRoom extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      rooms(p.currentRoom).print()
      (p, rooms)
    }
  }

  case class LookDir(dir:String) extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      val r = rooms(p.currentRoom)
      if(r.exits.contains(dir)) {
        rooms(r.exits(dir)).print()
      } else {
        println("That isn't a valid exit.")
      }
      (p, rooms)
    }
  }

  case class LookObject(item:String) extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      (p.items.find(_.matches(item)) orElse rooms(p.currentRoom).items.find(_.matches(item))) match {
        case None => println("You don't see that item.")
        case Some(i) => i.print()
      }
      (p, rooms)
    }
  }

  case object Inv extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      println("Your inventory includes:")
      p.items.foreach(i => println(i.name))
      (p, rooms)
    }
  }

  case object Help extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      println("Your current commands are: " + commands.mkString(", "))
      (p, rooms)
    }
  }
  
  case object Save extends Command {
    def apply(p: Player, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
      xml.XML.save("player.xml", p.toXML)
      (p, rooms)
    }
  }
}

case class Player(name: String, items: List[Item], currentRoom: String) extends Character {
  def process(input: String, rooms: Map[String, Room]): (Player, Map[String, Room]) = {
    if (rooms(currentRoom).exits.contains(input)) {
      val newRoom = rooms(currentRoom).exits(input)
      rooms(newRoom).print()
      (copy(currentRoom = newRoom), rooms)
    } else {
      val com = Player.parseAll(Player.command,input)
      if (com.successful) {
        com.get(this, rooms)
      } else {
        println("Invalid command.")
        (this, rooms)
      }
    }
  }

  def toXML: xml.Node = <player name={ name } currentRoom={ currentRoom }>
                          { items.map(_.toXML) }
                        </player>
}