package co.uk.redpixel.articles.routes

import cats.Monad
import cats.implicits.catsSyntaxFlatMapOps
import co.uk.redpixel.articles.algebra.NewsStore
import io.circe.Json
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

object HealthCheck {

  def routes[F[_] : Monad](store: NewsStore[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "internal" / "status" =>
        store.healthy >>= { status =>
          Ok(Json.obj("healthy" := status))
        }
    }
  }
}
