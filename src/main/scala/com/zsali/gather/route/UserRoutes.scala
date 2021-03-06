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

  def get(userService: UserService): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case req @ POST -> Root / create =>
        for {
          userReq <- req.as[UserReq]
          errorOrUser <- userService.createUser(userReq)
          res <- createUserToRes(errorOrUser)
        } yield res
      case req @ PUT -> Root / update / id =>
        for {
          userReq <- req.as[UserReq]
          errorOrUser <- userService.updateUser(id, userReq)
          res <- updateUserToRes(id, errorOrUser)
        } yield res
      case GET -> Root / id =>
        for {
          errorOrUser <- userService.getUser(id)
          res <- getUserToRes(id, errorOrUser)
        } yield res
      case DELETE -> Root / id =>
        for {
          errorOrRowsAffected <- userService.deleteUser(id)
          res <- deleteUserToRes(id, errorOrRowsAffected)
        } yield res
    }

  private def createUserToRes(errorOrUser: Either[Throwable, User]): IO[Response[IO]] =
    errorOrUser match {
      case Right(user) => {
        logger.debug("User created", Map("id" -> user.id))
        Created(user.asJson)
      }
      case Left(e) => {
        logger.error(s"Repo layer error --> ${e.getMessage()}")
        InternalServerError()
      }
    }

  private def updateUserToRes(id: String,
                              errorOrUser: Either[Throwable, Option[User]]
                             ): IO[Response[IO]] =
    errorOrUser match {
      case Right(Some(user)) => {
        logger.debug("User updated", Map("id" -> user.id))
        Ok(user.asJson)
      }
      case Left(e) => {
        logger.error(s"Repo layer error --> ${e.getMessage()}")
        InternalServerError()
      }
      case _ => {
        logger.debug("User not found so cannot be updated", Map("id" -> id))
        NotFound("User doesn't exist")
      }
    }

  private def getUserToRes(id: String,
                           errorOrUser: Either[Throwable, Option[User]]
                          ): IO[Response[IO]] =
    errorOrUser match {
      case Right(Some(user)) => {
        logger.debug("User found", Map("id" -> id))
        Ok(user.asJson)
      }
      case Left(e) => {
        logger.error(s"Repo layer error --> ${e.getMessage}")
        InternalServerError()
      }
      case _ => {
        logger.debug("User not found", Map("id" -> id))
        NotFound("User doesn't exist")
      }
    }

  private def deleteUserToRes(id: String,
                              errorOrRowsAffected: Either[Throwable, Int]
                             ): IO[Response[IO]] =
    errorOrRowsAffected match {
      case Right(1) => {
        logger.debug("User deleted", Map("id" -> id))
        NoContent()
      }
      case Left(e) => {
        logger.error(s"Repo layer error --> ${e.getMessage}")
        InternalServerError()
      }
      case _ => {
        logger.debug("User not found so cannot be deleted", Map("id" -> id))
        NotFound("User doesn't exist")
      }
    }
}