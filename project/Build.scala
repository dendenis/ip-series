import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._

object BuildSettings {
  val Organization = "interview"
  val Name         = "ip-series"
  val Version      = "0.0.1"
  val ScalaVersion = "2.11.8"

  val buildSettings = Seq(
    organization := Organization,
    name := Name,
    version := Version,
    scalaVersion := ScalaVersion
  )
}

object MainBuild extends Build {

  import BuildSettings._

  javacOptions ++= Seq("-encoding", "UTF-8")

  lazy val main = Project(
    Name,
    file("."),
    settings = buildSettings ++ Defaults.coreDefaultSettings ++
      Seq(
        mainClass in assembly := Some("series.Analysis")
      )
  )
}