package co.uk.redpixel.articles

import cats.effect.std.Dispatcher
import cats.effect.{Async, Resource}
import cats.syntax.all._
import co.uk.redpixel.articles.config.ApplicationConfig
import co.uk.redpixel.articles.persistence.{Database, QuillHeadlineStore}
import co.uk.redpixel.articles.routes.{GraphQL, HealthCheck}
import co.uk.redpixel.articles.schema.QuerySchema
import co.uk.redpixel.articles.scrape.Scraper
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.middleware.{Logger => PayloadLogger}
import org.typelevel.log4cats.Logger

object NewYorkTimesArticlesServer {

  def stream[F[_] : Async : Logger]: Stream[F, Nothing] = {
    for {
      // configuration
      config <- Stream.eval(ApplicationConfig.loadOrThrow[F])

      // database
      _ <- Stream.eval(Database.createSchema[F](config.db))

      headlines = QuillHeadlineStore[F]()

      // Scraping
      _ <- Stream.eval(Scraper(config.scraping) update headlines)

      // GraphQL
      dispatcher <- Stream.resource(Dispatcher[F])
      schema      = QuerySchema(dispatcher)
      graphQL     = SangriaGraphQL[F](schema, headlines)

      // routes
      routes = (
        HealthCheck.routes[F](headlines) <+>
        GraphQL.routes[F](graphQL)
      ).orNotFound

      // requests and responses logging
      httpApp = PayloadLogger.httpApp(logHeaders = true, logBody = true)(routes)

      exitCode <- Stream.resource(
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(config.server.httpPort)
          .withHttpApp(httpApp)
          .build >>
        Resource.eval(Async[F].never)
      )
    } yield exitCode
  }.drain
}
