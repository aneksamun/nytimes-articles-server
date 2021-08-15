package co.uk.redpixel.articles.config

import java.net.URL
import scala.concurrent.duration.FiniteDuration

final case class ScrapingConfig(nyTimesUrl: URL, repeatInterval: FiniteDuration)
