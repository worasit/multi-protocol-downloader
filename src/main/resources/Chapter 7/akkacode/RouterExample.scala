package akkacode

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.routing.BalancingPool
import java.awt.image.BufferedImage
import swing._

object RouterExample extends App {
  
//  val MaxCount = 10000
//  val ImageSize = 500
//
//  case class Complex(real: Double, imag: Double) {
//    def +(c: Complex) = Complex(real + c.real, imag + c.imag)
//    def *(c: Complex) = Complex(real * c.real - imag * c.imag, real * c.imag + imag * c.real)
//    def mag = math.sqrt(real * real + imag * imag)
//  }
//
//  def mandelStep(z: Complex, c: Complex): Complex = z * z + c
//
//  def mandelCount(c: Complex): Int = {
//    var cnt = 0
//    var z = c
//    while (cnt < MaxCount && z.mag < 4) {
//      z = mandelStep(z, c)
//      cnt += 1
//    }
//    cnt
//  }
//
//  case class MakeImage(xmin: Double, xmax: Double, ymin: Double, ymax: Double)
//  case class Line(row: Int, y: Double, xmin: Double, xmax: Double)
//  case class LineResult(row: Int, rgbs: Array[Int])
//
//  val img = new BufferedImage(ImageSize, ImageSize, BufferedImage.TYPE_INT_RGB)
//  val panel = new Panel {
//    override def paint(g: Graphics2D): Unit = {
//      g.drawImage(img, 0, 0, null)
//    }
//    preferredSize = new Dimension(ImageSize, ImageSize)
//  }
//
//  class MandelActor extends Actor {
//    def receive = {
//      case MakeImage(xmin, xmax, ymin, ymax) =>
//
//    }
//  }
//
//  class LineActor(mandel:ActorRef) extends Actor {
//    def receive = {
//      case Line(r, y, xmin, xmax) =>
//        mandel ! LineResult(r, Array.tabulate(ImageSize)(i => {
//          val x = xmin + i * (xmax - xmin) / ImageSize
//          mandelCount(Complex(x, y))
//        }))
//    }
//  }
//  
//  class ImageActor extends Actor {
//    def receive = {
//      case LineResult(r, rgbs) =>
//
//    }
//  }
//
//  val system = ActorSystem("Mandelbrot")
//  val actor = system.actorOf(Props[MandelActor], "MandelActor")
//  val router = system.actorOf(BalancingPool(5).props(Props(new LineActor(actor))),"poolRouter")
//
//  val frame = new MainFrame {
//    title = "Mandelbrot Set"
//    contents = panel
//  }
//
//  frame.open
}