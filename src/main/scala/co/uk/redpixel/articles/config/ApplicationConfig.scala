package co.uk.redpixel.articles.config

import cats.Applicative
import com.typesafe.config.ConfigFactory
import pureconfig.syntax.ConfigReaderOps

final case class ApplicationConfig(server: ServerConfig,
                                   scraping: ScrapingConfig,
                                   db: DatabaseConfig)

object ApplicationConfig {
  import pureconfig.generic.auto._
  import eu.timepit.refined.pureconfig._
  import co.uk.redpixel.articles.config.ServerConfig._

  def loadOrThrow[F[_]](implicit F: Applicative[F]): F[ApplicationConfig] =
    F.pure(ConfigFactory.load.toOrThrow[ApplicationConfig])
}
