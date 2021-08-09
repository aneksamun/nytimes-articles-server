package co.uk.redpixel.articles.persistence

import co.uk.redpixel.articles.algebra.NewsStore

class DoobieNewsStore[F[_]] extends NewsStore[F] {

  def healthy: F[Boolean] = ???
}
