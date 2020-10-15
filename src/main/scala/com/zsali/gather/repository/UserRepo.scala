package com.zsali.gather.repository

import java.{util => ju}

import cats._
import cats.data._
import cats.effect._
import cats.effect.implicits._
import com.zsali.gather.model.{User, UserReq}
import doobie._
import doobie.implicits._
import doobie.implicits.javatime.JavaTimeLocalDateMeta
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.Transactor
import java.time.format.DateTimeFormatter

import java.{time => jt}


/* Exceptions
    1. IOException - unrecoverable
    2. InvariantViolation - unrecoverable
    3. SQLException - some recoverable
 */

trait UserRepo {
  def addUser(user: UserReq): IO[Either[Throwable, User]]
  def getUser(id: String): IO[Either[Throwable, Option[User]]]
}

case class UserRepoImpl(xa: Transactor[IO]) extends UserRepo {

  override def addUser(user: UserReq) = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-d")
    val date = jt.LocalDate.parse(user.birthday, formatter)
    sql"""
      |insert into users (first_name, last_name, birthday, email)
      |values (${user.firstName}, ${user.lastName}, ${user.birthday}::date, ${user.email})
      |
       """.stripMargin
      .update
      .withUniqueGeneratedKeys[User]("id",
                                     "first_name",
                                     "last_name",
                                     "birthday",
                                     "email")
      .transact(xa)
      .attempt
  }

  override def getUser(id: String) =
    sql"""
      |select id,
      |       first_name,
      |       last_name,
      |       birthday,
      |       email
      |  from users
      | where id = ${ju.UUID.fromString(id)}
      |
       """.stripMargin
      .query[User]
      .option
      .transact(xa)
      .attempt
}
