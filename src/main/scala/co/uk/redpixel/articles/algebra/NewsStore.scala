package co.uk.redpixel.articles.algebra

trait NewsStore[F[_]] {

  def healthy: F[Boolean]
}
