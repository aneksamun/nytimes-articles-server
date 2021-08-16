package co.uk.redpixel.articles

import cats.effect.IO
import co.uk.redpixel.articles.scrape.Scraper
import co.uk.redpixel.articles.support.fixture.ServerDependencies
import org.scalatest.{GivenWhenThen, Outcome}
import org.scalatest.featurespec.{FixtureAnyFeatureSpec, FixtureAsyncFeatureSpec}
import org.scalatest.matchers.should.Matchers

class HeadlinesSpec extends FixtureAsyncFeatureSpec
  with ServerDependencies
  with GivenWhenThen
  with Matchers {

  Feature("Querying headlines") {

    info("As system user")
    info("I wish to scrap New Your Times headlines")
    info("So that I can query them later on")

    Scenario("") { headlineStore =>
      Given("")

//      Scraper.scrape(newYorkTimesServerMockUrl).update[IO](headlineStore)

      "a" should be("a")
    }
  }


}
