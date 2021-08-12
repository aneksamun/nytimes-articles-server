package co.uk.redpixel.articles

import cats.effect.Async
import co.uk.redpixel.articles.algebra.GraphQL

object SangriaGraphQL {

  def apply[F[_] : Async]: GraphQL[F] = ???
}
