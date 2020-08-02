// package com.zsali.gather.user

// import cats.effect._
// import cats.implicits._
// import doobie._
// import java.{util => ju}
// import doobie.implicits._
// import doobie.postgres._
// import doobie.postgres.implicits._

// trait UserRepo {
//   // def addUser(user: User): IO[User]
//   def getUser(id: String): IO[User]
//   // def getUsers: IO[Seq[Users]]
//   // def updateUser(user: User): IO[User]
// }

// case class UserRepoImpl(xa: Transactor[IO]) extends UserRepo {
//   override def getUser(id: String): IO[User] = {
//     sql"""
//           |select first_name,
//           |       last_name,
//           |       birthday,
//           |       email,
//           |       created_at,
//           |       updated_at
//           |  from users
//           | where id = ${ju.UUID.fromString(id)}
//           |
//         """.stripMargin
//            .query[User]
//            .unique
//            .transact(xa)
//   }      
// }