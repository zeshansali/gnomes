import Dependencies._

ThisBuild / scalaVersion     := "2.13.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.zsali"
ThisBuild / organizationName := "z-gnomes"

// addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.4.0-RC1-235-3231567a")
resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.sonatypeRepo("snapshots")  
)

lazy val root = (project in file("."))
  .settings(
    name := "gnomes",
    libraryDependencies ++= Seq(
      http4sDsl,
      http4sClient,
      http4sServer,
      scalaTest % Test
    )
  )