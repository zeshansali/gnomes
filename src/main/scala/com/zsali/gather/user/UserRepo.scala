package com.zsali.gather.user

import java.{util => ju}

import cats.effect.IO
import doobie.implicits._
import doobie.implicits.javatime.JavaTimeLocalDateMeta
// import doobie.postgres._
import doobie.postgres.implicits._
import doobie.Transactor

trait UserRepo {
  def getUser(id: String): IO[User]
}

case class UserRepoImpl(xa: Transactor[IO]) extends UserRepo {
  val y = xa.yolo
  import y._

  override def getUser(id: String): IO[User] = {
    sql"""
          |select first_name,
          |       last_name,
          |       birthday,
          |       email,
          |       created_at,
          |       updated_at
          |  from users
          | where id = ${ju.UUID.fromString(id)}
          |
       """.stripMargin
          .query[User]
          .unique
          .transact(xa)
  }      
}