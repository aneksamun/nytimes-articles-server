package co.uk.redpixel.articles.support.fixture

import cats.Id
import cats.effect.IO
import co.uk.redpixel.articles.algebra.HeadlineStore
import co.uk.redpixel.articles.config.ApplicationConfig
import co.uk.redpixel.articles.persistence.QuillHeadlineStore
import co.uk.redpixel.articles.support.Resources
import co.uk.redpixel.articles.support.fixture.ServerDependencies.Configuration
import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import com.dimafeng.testcontainers.{MockServerContainer, PostgreSQLContainer}
import eu.timepit.refined.auto._
import org.http4s.Status.Ok
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.scalatest.{FixtureAsyncTestSuite, FutureOutcome}

import java.net.URL
import scala.io.Source
import scala.util.Using

trait ServerDependencies extends TestContainersForAll with Resources {
  this: FixtureAsyncTestSuite =>

  lazy val newYorkTimesServerMockUrl: URL = withContainers { containers =>
    new URL(containers.tail.container.getEndpoint)
  }

  override type Containers = PostgreSQLContainer and MockServerContainer

  override type FixtureParam = HeadlineStore[IO]

  override def startContainers(): Containers =
    PostgreSQLContainer.Def(
      dockerImageName = "postgres:13.3-alpine",
      databaseName = Configuration.db.database,
      username = Configuration.db.user,
      password = Configuration.db.password
    ).start() and MockServerContainer.Def().start()

  override def afterContainersStart(containers: Containers): Unit =
    new MockServerClient(containers.tail.container.getHost, containers.tail.serverPort)
      .when(request()
        .withPath("/"))
      .respond(response()
        .withBody(
          Using.resource(Source.fromFile(loadFile("nytimes_landing_page.html")))(
            _.mkString
          )
        )
        .withStatusCode(Ok.code))

  override def withFixture(test: OneArgAsyncTest): FutureOutcome =
    test(QuillHeadlineStore[IO]())
}

object ServerDependencies {
  val Configuration: Id[ApplicationConfig] = ApplicationConfig.loadOrThrow[Id]
}
