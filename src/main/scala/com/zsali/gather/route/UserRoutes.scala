package com.zsali.gather.route

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import com.zsali.gather.model.{User, UserReq}
import com.zsali.gather.service.UserService
import org.http4s.dsl.impl.Responses.NotFoundOps
import com.zsali.jsonlogging.JsonLogger
import org.http4s.Response

object UserRoutes {
  implicit val decoder = jsonOf[IO, UserReq]

  // move this elsewhere, replace with log4cats at some point
  val logger = JsonLogger()

  def get(userService: UserService): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req @ POST -> Root / add => {
        for {
          userReq <- req.as[UserReq]
          maybeUser <- userService.addUser(userReq)
          res <- addUserToRes(maybeUser)
        } yield res
      }
      case GET -> Root / id => {
        for {
          maybeUser <- userService.getUser(id)
          res <- getUserToRes(id, maybeUser)
        } yield res
      }
    }
  }

  private def addUserToRes(user: Either[Throwable, User]): IO[Response[IO]] = {
    user match {
      case Left(e) => {
        logger.error(s"SOME ERROR --> ${e.getMessage()}")
        InternalServerError()
      }
      case Right(user) => Created(user.asJson)
    }
  }

  private def getUserToRes(id: String, user: Either[Throwable, Option[User]]): IO[Response[IO]] = {
    user match {
      case Left(e) => {
        logger.error(s"Database might be down --> ${e.getMessage}")
        InternalServerError()
      }
      case Right(maybeUser) =>
        maybeUser match {
          case Some(user) => {
            logger.debug("User found", Map("id" -> id))
            Ok(user.asJson)
          }
          case _ => {
            logger.debug("User not found", Map("id" -> id))
            NotFound("User doesn't exist")
          }
        }
    }
  }
}
