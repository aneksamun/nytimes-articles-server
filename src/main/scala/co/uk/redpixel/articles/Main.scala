package co.uk.redpixel.articles

import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {

  implicit val logger = Slf4jLogger.getLogger[IO]

  def run(args: List[String]): IO[ExitCode] =
    NewYorkTimesArticlesServer.stream[IO]
      .compile
      .drain.as(ExitCode.Success)
}
