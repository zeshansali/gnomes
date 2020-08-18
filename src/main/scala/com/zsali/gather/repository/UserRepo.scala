package com.zsali.gather.repository

import java.{util => ju}

import cats._
import cats.data._
import cats.effect._
import cats.effect.implicits._
import com.zsali.gather.model.User
import doobie._
import doobie.implicits._
import doobie.implicits.javatime.JavaTimeLocalDateMeta
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.Transactor

/* Exceptions
    1. IOException - unrecoverable
    2. InvariantViolation - unrecoverable
    3. SQLException - some recoverable
 */

trait UserRepo {
  def addUser(user: User): IO[Int]
  def getUser(id: String): IO[Either[Throwable, Option[User]]]
}

case class UserRepoImpl(xa: Transactor[IO]) extends UserRepo {

  override def getUser(id: String) = {
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
      .option
      .transact(xa)
      .attempt
  }

  override def addUser(user: User) = {
    sql"""
         |insert into users (first_name, last_name, birthday, email)
         |values(${user.firstName}, ${user.lastName}, ${user.birthday}, ${user.email})
         |
       """.stripMargin.update.run
      .transact(xa)
  }
}
