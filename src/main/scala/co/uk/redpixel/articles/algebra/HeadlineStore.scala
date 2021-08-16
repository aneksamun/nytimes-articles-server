package co.uk.redpixel.articles.algebra

import co.uk.redpixel.articles.algebra.HeadlineStore.Total
import co.uk.redpixel.articles.data.{Headline, Limit, Offset}

/** An algebra of operations in F that executes database requests. */
trait HeadlineStore[F[_]] {

  /**
    * Tests database connectivity
    *
    * @return the status whether connection is healthy
    */
  def isHealthy: F[Boolean]

  /**
    * Fetches headlines paginated by params
    *
    * @param offset the number of items to skip
    * @param limit total items to fetch
    * @return paginated result
    */
  def fetch(offset: Offset, limit: Limit): F[Seq[Headline]]

  /**
    * Stores headlines to the database
    *
    * @param headlines a batch of the headlines to persist
    * @return operation success or error
    */
  def add(headlines: Seq[Headline]): F[Total]
}

object HeadlineStore {
  type Total = Long
}
