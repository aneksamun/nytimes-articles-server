package co.uk.redpixel.articles

import cats.effect.IO
import cats.effect.std.Dispatcher
import cats.effect.unsafe.implicits.global
import co.uk.redpixel.articles.data.Headline
import co.uk.redpixel.articles.routes.GraphQL
import co.uk.redpixel.articles.schema.QuerySchema
import co.uk.redpixel.articles.scrape.Scraper
import co.uk.redpixel.articles.support.fixture.{FakeNewYorkTimesServer, HeadlinesStore, ServerDependencies}
import io.circe.Json
import io.circe.generic.auto._
import org.http4s.Method.POST
import org.http4s.Request
import org.http4s.circe.CirceEntityCodec._
import org.http4s.implicits.{http4sKleisliResponseSyntaxOptionT, http4sLiteralsSyntax}
import org.scalatest.featurespec.AsyncFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{EitherValues, GivenWhenThen}
import scalaz.Scalaz.ToIdOps

class HeadlinesSpec extends AsyncFeatureSpec
  with ServerDependencies
  with FakeNewYorkTimesServer
  with HeadlinesStore
  with GivenWhenThen
  with EitherValues
  with Matchers {

  Feature("Querying headlines") {

    info("As system user")
    info("I wish to scrap New Your Times headlines")
    info("So that I can query them later on")

    Scenario("Searching headlines") {
      Given("scraped New York Times headlines")
      val gatherHeadlines = setupNewYorkTimesServerMock |> Scraper.scrape |> update(headlinesStore)

      When("I query headlines")
      val request = Request[IO](POST, uri"/graphql") withEntity
        Json.obj("query" -> Json.fromString("{ news { title link } }"))

      val queryHeadlines = Dispatcher[IO].use { dispatcher =>
        GraphQL.routes(SangriaGraphQL[IO](
          QuerySchema(dispatcher),
          headlinesStore
        )).orNotFound(request)
      }

      Then("I see results")
      val news = for {
        response <- gatherHeadlines *> queryHeadlines
        body     <- response.as[Json]
        data      = body \\ "data" head
      } yield data \\ "news" head

      val headlines = news.map(_.as[Seq[Headline]]).unsafeRunSync().value

      headlines.forall(headline => headline.link.nonEmpty && headline.title.nonEmpty) should be(true)
    }
  }
}
