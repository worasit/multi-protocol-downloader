package akkacode

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.routing.BalancingPool
import java.awt.image.BufferedImage
import swing._
import java.awt.Color
import specmethods.Complex

object PoolExample extends App {
  val MaxCount = 10000
  val ImageSize = 500

  def mandelStep(z: Complex, c: Complex): Complex = z * z + c

  def mandelCount(c: Complex): Int = {
    var cnt = 0
    var z = c
    while (cnt < MaxCount && z.magnitude < 4) {
      z = mandelStep(z, c)
      cnt += 1
    }
    cnt
  }

  case class MakeImage(xmin: Double, xmax: Double, ymin: Double, ymax: Double)
  case class Line(row: Int, y: Double, xmin: Double, xmax: Double)
  case class LineResult(row: Int, rgbs: Array[Int])

  val img = new BufferedImage(ImageSize, ImageSize, BufferedImage.TYPE_INT_RGB)
  val panel = new Panel {
    override def paint(g: Graphics2D): Unit = {
      g.drawImage(img, 0, 0, null)
    }
    preferredSize = new Dimension(ImageSize, ImageSize)
  }

  class MandelActor extends Actor {
    val router = system.actorOf(BalancingPool(4).props(Props[LineActor]),"poolRouter")
    var waiting = (0 until ImageSize).toSet
    val start = System.nanoTime
    def receive = {
      case MakeImage(xmin, xmax, ymin, ymax) =>
        for(i <- 0 until ImageSize) {
          val y = ymin + i*(ymax-ymin)/ImageSize
          router ! Line(i,y,xmin,xmax)
        }
      case LineResult(r, rgbs) =>
      	for(i <- rgbs.indices) img.setRGB(i,r,rgbs(i))
      	waiting -= r
      	if(waiting.isEmpty) {
      	  panel.repaint
      	  println("Time = "+(System.nanoTime()-start)/1e9)
      	}
    }
  }

  class LineActor extends Actor {
    def receive = {
      case Line(r, y, xmin, xmax) =>
        sender ! LineResult(r, Array.tabulate(ImageSize)(i => {
          val x = xmin + i * (xmax - xmin) / ImageSize
          val cnt = mandelCount(Complex(x, y))
          new Color(cnt.toFloat/MaxCount,0f,0f).getRGB
        }))
    }
  }
  
  val system = ActorSystem("Mandelbrot")
  val mandelActor = system.actorOf(Props[MandelActor], "MandelActor")
  
  mandelActor ! MakeImage(-1.5,0.5,-1,1)

  val frame = new MainFrame {
    title = "Mandelbrot Set"
    contents = panel
    centerOnScreen
  }

  frame.open
  frame.visible = true
}