import sbt._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.nongped",
      scalaVersion := "2.12.2",
      version := "0.1.0-SNAPSHOT" // setting expression
      // key - operator - body
    )),
    showSuccess := true,
    name := "multi-protocol-downloader",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    libraryDependencies += "org.powermock" % "powermock-api-mockito" % "1.6.5"
  )

//
//name := "multi-protocol-downloader"
//
//version := "1.0"
//
//scalaVersion := "2.12.2"


//import Dependencies._
//import Keys._
//
//// All of this are called scala DSL (Domain-Specific Language).
//
//// This one is called "subproject"
//lazy val root = (project in file(".")).
//  settings(
//    inThisBuild(List(
//      organization := "com.example",
//      scalaVersion := "2.12.1",
//      version := "0.1.0-SNAPSHOT" // setting expression
//      // key - operator - body
//    )),
//    showSuccess := true,
//    name := "Hello",
//    libraryDependencies += scalaTest % Test,
//    libraryDependencies += "org.powermock" % "powermock-api-mockito" % "1.6.5"
//
//  )


//package example
//
//import org.scalatest._
//
//class HelloSpec extends FlatSpec with Matchers {
//
//  "The Hello object" should "say hello" in {
//    Hello.greeting shouldEqual "hello"
//  }
//
//
//  "The Hello Object" should "say eating" in {
//    Hello.eating shouldEqual "eating"
//  }
//
//}