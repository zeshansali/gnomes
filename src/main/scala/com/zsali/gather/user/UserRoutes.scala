package com.zsali.gather.user

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._

object UserRoutes {
  def get(userRepo: UserRepo): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET  -> Root / id => {
        userRepo
          .getUser(id)
          .flatMap(user => Ok(user.asJson))
      }
    }
  }
}