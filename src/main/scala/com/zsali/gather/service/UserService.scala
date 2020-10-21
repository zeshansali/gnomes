package com.zsali.gather.service

import cats.effect.IO
import com.zsali.gather.model.{User, UserReq}
import com.zsali.gather.repository.UserRepo

trait UserService {
  def createUser(user: UserReq): IO[Either[Throwable, User]]
  def updateUser(id: String, user: UserReq): IO[Either[Throwable, Option[User]]]
  def getUser(id: String): IO[Either[Throwable, Option[User]]]
  def deleteUser(id: String): IO[Either[Throwable, Int]]
}

final case class UserServiceImpl(repo: UserRepo) extends UserService {
  // add error handling for when the user wasn't created
  def createUser(user: UserReq) = repo.createUser(user)
  def updateUser(id: String, user: UserReq) = repo.updateUser(id, user)
  def getUser(id: String) = repo.getUser(id)
  def deleteUser(id: String) = repo.deleteUser(id)
}
