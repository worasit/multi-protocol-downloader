package com.learning.chaptertwo

import scala.io.{BufferedSource, Source}


object Iterator extends App with Description {
  initDescription()

  val source: BufferedSource = Source.fromResource("Chapter 2/TX417945_1263.csv")

  def parseLine(line: String): TempData = {
    val data = line.trim.split(",")

    if (data.length > 1) {
      TempData(data(1).toInt, data(4).toInt, data(5).toDouble, data(6).toInt, data(7).toInt, data(8).toInt)
    } else {
      null
    }
  }

  source.getLines.map(parseLine).foreach(tempData => {
    printTempData(tempData)
  })

  source.close
}


trait Description {
  lazy val section: String = "Section 2, Lesson 25"

  def initDescription(): String = {
    println(section)
    section
  }

  def printTempData(tempData: TempData): Unit = {
    if (TempData != null)
      println(s"day:${tempData.day} year:${tempData.precip} precip:${tempData.precip} avgTmp:${tempData.aveTemp} minTmp:${tempData.minTemp} maxTmp:${tempData.maxTemp}")
  }
}

case class TempData(day: Int, year: Int, precip: Double, aveTemp: Int, maxTemp: Int, minTemp: Int)