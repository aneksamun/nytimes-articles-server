package co.uk.redpixel.articles.persistence

import cats.effect.Async
import cats.syntax.all._
import co.uk.redpixel.articles.algebra.HeadlinesStore
import co.uk.redpixel.articles.data.{Headlines, Limit, Offset}

import scala.concurrent.ExecutionContext
import com.github.mauricio.async.db.util.ExecutorServiceUtils._
import io.getquill.{Literal, NamingStrategy, PostgresAsyncContext}

class QuillHeadlinesStore[F[_] : Async, N <: NamingStrategy] private(ctx: PostgresAsyncContext[N]) extends HeadlinesStore[F] {
  import ctx._
  implicit val ec: ExecutionContext = CachedExecutionContext

  def isHealthy: F[Boolean] =
    Async[F]
      .fromFuture(Async[F].delay(ctx.run(quote(infix"SELECT true".as[Boolean]))))
      .recoverWith { _ =>
        false.pure[F]
      }

  def fetch(offset: Offset, limit: Limit): F[Seq[Headlines]] = Async[F].fromFuture {
    Async[F] delay ctx.run(quote {
      query[Headlines].drop(lift(offset.value)).take(lift(limit.value)).sortBy(_.title)
    })
  }
}

object QuillHeadlinesStore {

  def apply[F[_]: Async](): HeadlinesStore[F] =
    new QuillHeadlinesStore(new PostgresAsyncContext(Literal, configPrefix = "db"))
}
