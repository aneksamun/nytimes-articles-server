package co.uk.redpixel.articles.persistence

import co.uk.redpixel.articles.algebra.NewsStore
import com.zaxxer.hikari.HikariDataSource
import io.getquill.{Literal, NamingStrategy, PostgresJdbcContext}

class QuillNewsStore[F[_], N <: NamingStrategy] private(ctx: PostgresJdbcContext[N]) extends NewsStore[F] {
  import ctx._

  def healthy: F[Boolean] = ???
}

object QuillNewsStore {

  def apply[F[_]](dataSource: HikariDataSource): NewsStore[F] =
    new QuillNewsStore(new PostgresJdbcContext(Literal, dataSource))
}
