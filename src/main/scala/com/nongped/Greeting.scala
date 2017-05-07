package com.nongped


object Application extends Application with App {
  println(greetingMsg)
}

trait Application {
  lazy val greetingMsg: String = "Hello Scala"
}


