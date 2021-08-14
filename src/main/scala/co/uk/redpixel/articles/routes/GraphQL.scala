package co.uk.redpixel.articles.routes

import cats.effect.Async
import cats.syntax.all._
import co.uk.redpixel.articles.algebra.GraphQL
import io.circe.Json
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{HttpRoutes, StaticFile}

object GraphQL {

  def routes[F[_] : Async](graphQL: GraphQL[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "graphql" =>
        req.as[Json].flatMap(graphQL.query).flatMap {
          case Right(json) => Ok(json)
          case Left(json)  => BadRequest(json)
        }

      case GET -> Root / "playground.html" =>
        StaticFile
          .fromResource[F]("/assets/playground.html")
          .getOrElseF(NotFound())

      case _ =>
        PermanentRedirect(Location(uri"/playground.html"))
    }
  }
}
