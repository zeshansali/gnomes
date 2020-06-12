import sbt._

object Dependencies {
  import Library._

  def get = List(
    http4sClient,
    http4sDsl,
    http4sServer,
    logbackClassic,
    logstash,
    scalaTest % "test"
  )
}

object Versions {
  val scala      = "2.13.2"
  val crossScala = List("2.11.12", "2.12.8", scala)

  val http4s     = "0.21.4"
  val logback    = "1.2.3"
  val logstash   = "5.2"
  val scalaTest  = "3.1.1"
}

object Library {
  lazy val http4sClient   = "org.http4s"           %% "http4s-blaze-client"      % Versions.http4s
  lazy val http4sDsl      = "org.http4s"           %% "http4s-dsl"               % Versions.http4s
  lazy val http4sServer   = "org.http4s"           %% "http4s-blaze-server"      % Versions.http4s

  // logging dependencies
  lazy val logbackClassic = "ch.qos.logback"       %  "logback-classic"          % Versions.logback
  lazy val logstash       = "net.logstash.logback" %  "logstash-logback-encoder" % Versions.logstash

  // testing dependencies
  lazy val scalaTest      = "org.scalatest"        %% "scalatest"                % Versions.scalaTest
}