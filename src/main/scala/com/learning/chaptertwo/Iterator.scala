package com.learning.chaptertwo


object Iterator extends App with Description {
  initDescription()




}


trait Description {

  lazy val section: String = "Section 2, Lesson 25"

  def initDescription(): Unit = println(section)
}