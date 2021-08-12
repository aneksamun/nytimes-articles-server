package co.uk.redpixel.articles.algebra

import co.uk.redpixel.articles.data.Headlines

trait HeadlinesStore[F[_]] {

  def healthy: F[Boolean]

  def listAll(): F[Seq[Headlines]]
}
