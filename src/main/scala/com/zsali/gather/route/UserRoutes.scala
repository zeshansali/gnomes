package com.zsali.gather.route

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
// import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import doobie.postgres.free.largeobjectmanager.LargeObjectManagerOp.Create
import com.zsali.gather.model.User
import com.zsali.gather.service.UserService
import org.http4s.dsl.impl.Responses.NotFoundOps

object UserRoutes {
  implicit val decoder = jsonOf[IO, User]

  def get(userService: UserService): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      // case req @ POST -> Root / create => {
      //   for {
      //     user <- req.as[User]
      //     response <- userService.addUser(user)
      //     _ <- Created()
      //   } yield
      // }
      case GET -> Root / id => {
        userService
          .getUser(id)
          .flatMap(_ match {
            case Some(user) => Ok(user.asJson)
            case _ => NotFound()
          })
      }
    }
  }
}