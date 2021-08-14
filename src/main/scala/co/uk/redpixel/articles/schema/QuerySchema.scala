package co.uk.redpixel.articles.schema

import cats.effect.std.Dispatcher
import co.uk.redpixel.articles.algebra.HeadlinesStore
import sangria.schema.Schema

object QuerySchema {

  def apply[F[_]](dispatcher: Dispatcher[F]): Schema[HeadlinesStore[F], Unit] =
    Schema(QueryType(dispatcher))
}
