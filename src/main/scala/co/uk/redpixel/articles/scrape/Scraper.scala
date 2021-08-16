package co.uk.redpixel.articles.scrape

import cats.effect.kernel.{Concurrent, Fiber, Temporal}
import cats.syntax.all._
import co.uk.redpixel.articles.algebra.HeadlineStore
import co.uk.redpixel.articles.config.ScrapingConfig
import co.uk.redpixel.articles.data.Headline
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import org.typelevel.log4cats.Logger

import java.net.URL
import scala.concurrent.duration.{DurationInt, FiniteDuration}

object Scraper {

  def apply(config: ScrapingConfig): Builder =
    scrape(config.nyTimesUrl).repeatEvery(config.repeatInterval)

  def scrape(scrapeUrl: URL): Builder =
    Builder(scrapeUrl)

  final case class Builder(scrapeUrl: URL, repeatInterval: FiniteDuration = 4 hours) {

    def repeatEvery(repeatInterval: FiniteDuration): Builder =
      copy(repeatInterval = repeatInterval)

    def update[F[_] : Concurrent : Logger](store: HeadlineStore[F])(implicit t: Temporal[F]): F[Fiber[F, Throwable, Unit]] = {

      def loadPage = JsoupBrowser().get(scrapeUrl.toString)

      def extractHeadlines(doc: Browser#DocumentType) = {
        val links = doc >> extractor("a.css-t059uy")

        links
          .map((element: Element) =>
            Headline(
              link  = element >> attr("href"),
              title = element >> text
            )
          )
          .filter(!_.title.isBlank)
          .toList
      }

      Logger[F].info(s"Scraping headlines from New York Times...") *>
        Concurrent[F].start(
          fs2.Stream(loadPage |> extractHeadlines)
             .evalTap(headlines =>
               Logger[F] info s"Total collected: ${headlines.length}")
             .evalMap(store add _)
             .evalTap(total =>
               Logger[F] info s"Updated: $total")
             .evalTap(_ => Concurrent[F].sleep(5 seconds))
            .repeat
            .compile
            .drain.void
        )
    }
  }
}
