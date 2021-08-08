package co.uk.redpixel.articles.config

import java.net.URL
import scala.concurrent.duration.Duration

final case class ScrapingConfig(nyTimesUrl: URL, repeatInterval: Duration)
