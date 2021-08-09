package co.uk.redpixel.articles.routes

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{HttpRoutes, StaticFile}

object GraphQL {

  def routes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "playground.html" =>
        StaticFile
          .fromResource[F]("/assets/playground.html")
          .getOrElseF(NotFound())

      case _ =>
        PermanentRedirect(Location(uri"/playground.html"))
    }
  }
}
