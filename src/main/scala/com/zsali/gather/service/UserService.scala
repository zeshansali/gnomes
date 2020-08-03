package com.zsali.gather.service

import cats.effect.IO
import com.zsali.gather.model.User
import com.zsali.gather.repository.UserRepo

trait UserService {
  def addUser(user: User): IO[Int]
  def getUser(id: String): IO[Option[User]]
}

final case class UserServiceImpl(repo: UserRepo) extends UserService {
  def addUser(user: User) = repo.addUser(user)
  def getUser(id: String) = repo.getUser(id)
}