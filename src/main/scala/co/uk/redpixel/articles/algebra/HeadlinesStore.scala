package co.uk.redpixel.articles.algebra

trait HeadlinesStore[F[_]] {

  def healthy: F[Boolean]
}
