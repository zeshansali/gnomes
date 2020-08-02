package com.zsali.gather

import java.{util => ju}

import cats.effect._
import doobie.Transactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import com.zsali.gather.user.{UserRepo, UserRepoImpl, UserRoutes}

object Main extends IOApp {
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  val xa = Transactor.fromDriverManager[IO]("org.postgresql.Driver", "jdbc:postgresql:local", "postgres", "password")
  val userRepo: UserRepo = UserRepoImpl(xa)

  val httpApp = Router(
    "/api/v1/users" -> UserRoutes.get(userRepo)
  ).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}