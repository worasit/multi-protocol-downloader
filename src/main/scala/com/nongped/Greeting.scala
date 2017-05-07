package com.nongped

import scala.io.Source


object Application extends Greeting with App {
  println(greetingMsg)
}

trait Greeting {
  lazy val greetingMsg: String = "Hello Scala"
}


