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
