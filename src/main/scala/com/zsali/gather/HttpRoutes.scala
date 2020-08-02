package com.zsali.gather

import java.{util => ju}
import java.time.LocalDate

import cats._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.free.Embedded.Connection
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.util.ExecutionContexts
import io.circe.Encoder
import io.circe.generic.auto._
import io.circe.literal._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.Router

case class User(firstName: String,
                lastName: String,
                birthday: LocalDate,
                email: String,
                createdAt: String,
                updatedAt: String)

trait UserRepo {
  def getUser(id: String): IO[User]
}

case class UserRepoImpl(xa: Transactor[IO]) extends UserRepo {
  override def getUser(id: String): IO[User] = {
    // sql"""
    //       |select first_name,
    //       |       last_name,
    //       |       birthday,
    //       |       email,
    //       |       created_at,
    //       |       updated_at
    //       |  from users
    //       | where id = ${ju.UUID.fromString(id)}
    //       |
    //    """.stripMargin
    //       .query[User]
    //       .unique
    //       .transact(xa)
    IO(User("Zeshan", "Ali", LocalDate.now(), "hello@gmail.com", "", ""))
  }      
}

object UserRoutes {
  import User._

  def get(userRepo: UserRepo): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET  -> Root / id => {
        userRepo.getUser(id).flatMap(user => Ok(user.asJson))
      }
    }
  }
}

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