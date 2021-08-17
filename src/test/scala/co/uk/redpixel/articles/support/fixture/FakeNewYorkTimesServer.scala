package co.uk.redpixel.articles.support.fixture

import cats.effect.IO
import cats.effect.unsafe.IORuntime.global
import co.uk.redpixel.articles.algebra.HeadlineStore
import co.uk.redpixel.articles.scrape.Scraper
import co.uk.redpixel.articles.support.Resources
import com.dimafeng.testcontainers.MockServerContainer
import org.http4s.Status.Ok
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.net.URL
import scala.concurrent.duration.DurationInt
import scala.io.Source
import scala.util.Using

trait FakeNewYorkTimesServer extends Resources {
  this: ServerDependencies =>

  implicit val logger = Slf4jLogger.getLogger[IO]

  def configure(mock: MockServerContainer): URL = {
    client = new MockServerClient(mock.container.getHost, mock.serverPort)

    client
      .when(request().withPath("/"))
      .respond(response()
        .withBody(
          Using.resource(Source.fromFile(loadFile("nytimes_landing_page.html")))(
            _.mkString
          )
        ).withStatusCode(Ok.code))

    new URL(mock.container.getEndpoint)
  }

  def update(headlines: HeadlineStore[IO])(scraper: Scraper.Builder): Unit = {
    val scrapeAndStoreHeadlines = for {
      fiber <- scraper.update[IO](headlines).start
      _ <- IO.sleep(3 seconds)
      _ <- fiber.cancel
    } yield ()

    scrapeAndStoreHeadlines.unsafeRunSync()(global)
  }

  var client: MockServerClient = _
}
