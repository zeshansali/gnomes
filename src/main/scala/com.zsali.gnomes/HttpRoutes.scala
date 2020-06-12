package com.zsali.gnomes

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.Router
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {
  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name.")
  }

  val tweetService = HttpRoutes.of[IO] {
    case GET -> Root / "tweets" / "popular" => Tweet.getPopularTweets().flatMap(Ok(_))
    case GET -> Root / "tweets" / IntVar(tweetId)=> Tweet.getTweet(tweetId).flatMap(Ok(_))
  }

  val services = tweetService <+> helloWorldService
  val httpApp = Router("/" -> helloWorldService, "/api" -> services).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

object Tweet {
  case class Tweet(id: Int, message: String)

  implicit def tweetEncoder: EntityEncoder[IO, Tweet] = ???
  implicit def tweetsEncoder: EntityEncoder[IO, Seq[Tweet]] = ???

  def getTweet(tweetId: Int): IO[Tweet] = ???
  def getPopularTweets(): IO[Seq[Tweet]] = ???
}