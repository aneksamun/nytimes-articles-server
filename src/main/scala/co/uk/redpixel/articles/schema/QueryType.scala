package co.uk.redpixel.articles.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import co.uk.redpixel.articles.algebra.HeadlinesStore
import sangria.schema._

object QueryType {

  def apply[F[_] : Async](effectDispatcher: Dispatcher[F]): ObjectType[HeadlinesStore[F], Unit] =
    ObjectType(
      name = "Query",
      fields = fields(
        Field(
          name = "news",
          fieldType = ListType(NewsType()),
          description = Some("The New York Times headlines"),
          resolve = c => effectDispatcher.unsafeToFuture(c.ctx.listAll())
        ))
    )
}
