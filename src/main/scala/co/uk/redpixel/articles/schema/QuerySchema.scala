package co.uk.redpixel.articles.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import co.uk.redpixel.articles.algebra.HeadlineStore
import sangria.schema.Schema

object QuerySchema {

  def apply[F[_]: Async](dispatcher: Dispatcher[F]): Schema[HeadlineStore[F], Unit] =
    Schema(QueryType(dispatcher))
}
