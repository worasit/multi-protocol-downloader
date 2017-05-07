package com.learning.chaptertwo

import scala.io.{BufferedSource, Source}


object Iterator extends Description {

  initDescription()

  val source: BufferedSource = Source.fromResource("Chapter 2/TX417945_1263.csv")



  source.close
}


trait Description {

  lazy val section: String = "Section 2, Lesson 25"


  def initDescription(): String = {
    println(section)
    section
  }
}