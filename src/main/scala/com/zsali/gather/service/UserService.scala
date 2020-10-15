package com.zsali.gather.service

import cats.effect.IO
import com.zsali.gather.model.{User, UserReq}
import com.zsali.gather.repository.UserRepo

trait UserService {
  def addUser(user: UserReq): IO[Either[Throwable, User]]
  def getUser(id: String): IO[Either[Throwable, Option[User]]]
  def deleteUser(id: String): IO[Either[Throwable, Int]]
}

final case class UserServiceImpl(repo: UserRepo) extends UserService {
  // add error handling for when the user wasn't created
  def addUser(user: UserReq) = repo.addUser(user)
  def getUser(id: String) = repo.getUser(id)
  def deleteUser(id: String) = repo.deleteUser(id)
}
