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
      case DELETE -> Root / id => {
        for {
          maybeRowsAffected <- userService.deleteUser(id)
          res <- deleteUserToRes(id, maybeRowsAffected)
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

  private def deleteUserToRes(id: String, maybeRowsAffected: Either[Throwable, Int]): IO[Response[IO]] = {
    maybeRowsAffected match {
      case Left(e) => {
        logger.error(s"Database might be down --> ${e.getMessage}")
        InternalServerError()
      }
      case Right(rowsAffected) =>
        rowsAffected match {
          case 0 => {
            logger.debug("User not found so cannot be deleted", Map("id" -> id))
            NotFound("User doesn't exist")
          }
          case _ => {
            logger.debug("User deleted", Map("id" -> id))
            NoContent()
          }
        }
    }
  }
}
