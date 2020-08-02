// package com.zsali.gather.user

// import cats.effect._
// import io.circe.syntax._
// import org.http4s.dsl.io._
// import org.http4s.HttpRoutes
// import io.circe._
// import io.circe.generic.semiauto._
// import java.{util => ju}

// import cats.implicits._
// import doobie._
// import doobie.implicits._
// import doobie.postgres._
// import doobie.postgres.implicits._

// // user
// case class User(firstName: String,
//                 lastName: String,
//                 birthday: ju.Date,
//                 email: String,
//                 createdAt: String,
//                 updatedAt: String)

// object User {
//   implicit val decoder: Decoder[User] = deriveDecoder  
//   implicit val encoder: Encoder[User] = deriveEncoder
// }

// // repo
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

// // routes
// object UserRoutes {
//   import User._

//   def routes(userRepo: UserRepo): HttpRoutes[IO] = {
//     HttpRoutes.of[IO] {
//       case GET  -> Root      => Ok()
//       case POST -> Root      => Ok()
//       case PUT  -> Root / id => Ok()
//       case GET  -> Root / id => {
//         val userIo = userRepo.getUser(id).flatMap(_.asJson)
//         Ok("boom")
//         // userIo.flatMap(user => Ok(user.asJson))
//       }
//     }
//   }
// }