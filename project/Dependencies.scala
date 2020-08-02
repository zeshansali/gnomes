import sbt._

object Dependencies {
  import Library._

  def get = List(
    circeCore,
    circeGeneric,
    circeLiteral,
    circeParser,
    doobie,
    doobiePostgres,
    http4sCirce,
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

  val circe      = "0.13.0"
  val doobie     = "0.8.8"
  val http4s     = "0.21.4"
  val logback    = "1.2.3"
  val logstash   = "5.2"
  val scalaTest  = "3.1.1"
}

object Library {
  // app dependencies
  lazy val circeCore      = "io.circe"             %% "circe-core"               % Versions.circe
  lazy val circeGeneric   = "io.circe"             %% "circe-generic"            % Versions.circe
  lazy val circeLiteral   = "io.circe"             %% "circe-literal"            % Versions.circe
  lazy val circeParser    = "io.circe"             %% "circe-parser"             % Versions.circe
  lazy val doobie         = "org.tpolecat"         %% "doobie-core"              % Versions.doobie
  lazy val doobiePostgres = "org.tpolecat"         %% "doobie-postgres"          % Versions.doobie
  lazy val http4sCirce    = "org.http4s"           %% "http4s-circe"             % Versions.http4s
  lazy val http4sClient   = "org.http4s"           %% "http4s-blaze-client"      % Versions.http4s
  lazy val http4sDsl      = "org.http4s"           %% "http4s-dsl"               % Versions.http4s
  lazy val http4sServer   = "org.http4s"           %% "http4s-blaze-server"      % Versions.http4s

  // logging dependencies
  lazy val logbackClassic = "ch.qos.logback"       %  "logback-classic"          % Versions.logback
  lazy val logstash       = "net.logstash.logback" %  "logstash-logback-encoder" % Versions.logstash

  // testing dependencies
  lazy val scalaTest      = "org.scalatest"        %% "scalatest"                % Versions.scalaTest
}