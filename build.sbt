ThisBuild / scalaVersion := Versions.scala
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.zsali"
ThisBuild / organizationName := "zsali"

resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val root = (project in file("."))
  .settings(
    name := "gather",
    libraryDependencies ++= Dependencies.get
  )
