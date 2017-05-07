package akkamud

import akka.actor.{ Actor, ActorSystem, Props, ActorRef }
import collection.mutable
import java.net.Socket
import scala.concurrent.duration._
import akka.util.Timeout
import java.io.File
import java.net.ServerSocket
import java.io.InputStream
import java.io.PrintStream

object ServerMain extends App {
  case class NewUser(sock: Socket)
  case class Startup(mapFile: File, playerFile: File)
  case object Tick

  case class ActivePlayer(sock: Socket, in: InputStream, name: String, actor: ActorRef)

  class ServerActor extends Actor {
    implicit val ec = context.dispatcher

    private val users = mutable.Buffer[ActivePlayer]()
    private var rooms: Map[String, (String, ActorRef)] = null
    private val ticker = context.system.scheduler.schedule(100.millis, 100.millis, self, Tick)

    def receive = {
      case NewUser(sock) =>
        val ap = ActivePlayer(sock, sock.getInputStream(), null, context.actorOf(Props(new Player(new PrintStream(sock.getOutputStream())))))
        users += ap
        ap.actor ! Player.Print("What is your name?")
      case Startup(map, players) =>
        val mapXML = xml.XML.loadFile(map)
        rooms = (mapXML \ "room").map(n => (n \ "@keyword").text -> ((n \ "@name").text, context.actorOf(Props(new Room(n))))).toMap
        for ((_, (name, room)) <- rooms) room ! Room.LinkRooms(rooms)
      case Tick =>
        for (i <- users.indices) {
          val user = users(i)
          if (user.in.available() > 0) {
            val buf = new Array[Byte](user.in.available)
            user.in.read(buf)
            val command = new String(buf).trim
            if (user.name == null) {
              if (command.nonEmpty) {
                users(i) = user.copy(name = command)
                users(i).actor ! Player.SetName(command)
                rooms("Inn")._2 ! Room.Arrive(command, users(i).actor)
              }
            } else {
              user.actor ! Player.Command(command)
            }
          }
        }
    }
  }

  implicit val timeout = Timeout(5.seconds)
  val system = ActorSystem.apply("AkkaMUD")
  val server = system.actorOf(Props[ServerActor], "MUDServer")

  server ! Startup(new File("map2.xml"), null) // TODO

  val ss = new ServerSocket(4040)
  while (true) {
    val sock = ss.accept
    server ! NewUser(sock)
  }
}