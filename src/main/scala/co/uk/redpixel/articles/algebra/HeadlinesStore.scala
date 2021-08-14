package co.uk.redpixel.articles.algebra

import co.uk.redpixel.articles.data.{Headlines, Limit, Offset}

trait HeadlinesStore[F[_]] {

  def isHealthy: F[Boolean]

  def fetch(offset: Offset, limit: Limit): F[Seq[Headlines]]
}
