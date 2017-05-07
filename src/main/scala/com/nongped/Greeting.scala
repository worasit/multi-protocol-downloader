package com.nongped


object Application extends Greeting with App {
  println(greetingMsg)
}

trait Greeting {
  lazy val greetingMsg: String = "Hello Scala"
}


