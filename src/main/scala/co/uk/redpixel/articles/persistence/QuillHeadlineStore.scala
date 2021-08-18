package co.uk.redpixel.articles.persistence

import cats.effect.Async
import cats.syntax.all._
import co.uk.redpixel.articles.algebra.HeadlineStore.Total
import co.uk.redpixel.articles.algebra.HeadlineStore
import co.uk.redpixel.articles.data.{Headline, Limit, Offset}
import com.github.mauricio.async.db.util.ExecutorServiceUtils._
import com.typesafe.config.Config
import io.getquill.{Literal, NamingStrategy, PostgresAsyncContext}

import scala.concurrent.ExecutionContext

class QuillHeadlineStore[F[_] : Async, N <: NamingStrategy] private(ctx: PostgresAsyncContext[N]) extends HeadlineStore[F] {
  import ctx._
  implicit val ec: ExecutionContext = CachedExecutionContext

  def isHealthy: F[Boolean] =
    Async[F]
      .fromFuture(Async[F].delay(ctx.run(quote(infix"SELECT true".as[Boolean]))))
      .recoverWith { _ =>
        false.pure[F]
      }

  def fetch(offset: Offset, limit: Limit): F[Seq[Headline]] = Async[F].fromFuture {
    Async[F] delay ctx.run(quote {
      querySchema[Headline]("headlines")
        .drop(lift(offset.value))
        .take(lift(limit.value))
        .sortBy(_.title)
    })
  }

  def add(articles: Seq[Headline]): F[Total] = Async[F].fromFuture {
    Async[F] delay ctx.run(quote {
      liftQuery(articles).foreach(
        querySchema[Headline]("headlines").insert(_).onConflictIgnore
      )
    }).map(_.sum)
  }
}

object QuillHeadlineStore {

  def apply[F[_]: Async](): HeadlineStore[F] =
    new QuillHeadlineStore(new PostgresAsyncContext(Literal, configPrefix = "db"))

  def apply[F[_]: Async](config: Config): HeadlineStore[F] =
    new QuillHeadlineStore(new PostgresAsyncContext(Literal, config))
}
