package co.uk.redpixel.articles

import co.uk.redpixel.articles.scrape.Scraper
import co.uk.redpixel.articles.support.fixture.{FakeNewYorkTimesServer, HeadlinesStore, ServerDependencies}
import com.dimafeng.testcontainers.MockServerContainer
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpec
import org.scalatest.matchers.should.Matchers
import scalaz.Scalaz.ToIdOps

class HeadlinesSpec extends AsyncFeatureSpec
  with ServerDependencies
  with FakeNewYorkTimesServer
  with HeadlinesStore
  with GivenWhenThen
  with Matchers {

  Feature("Querying headlines") {

    info("As system user")
    info("I wish to scrap New Your Times headlines")
    info("So that I can query them later on")

    Scenario("Searching headlines") {
      //      Request[IO](POST, uri"/graphql", body = )
      "a" should be("a")
    }
  }

  def onceStarted(mock: MockServerContainer): Unit =
    configure(mock) |> Scraper.scrape |> update(headlinesStore)

  def onShutdown(): Unit =
    client.stop()
}
