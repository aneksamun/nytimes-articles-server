package co.uk.redpixel.articles.schema

import cats.effect.kernel.Async
import cats.effect.std.Dispatcher
import cats.syntax.all._
import co.uk.redpixel.articles.algebra.HeadlinesStore
import co.uk.redpixel.articles.data.{Limit, Offset}
import sangria.schema._

object QueryType {

  val OffsetArgument = Argument("offset", OptionInputType(IntType), defaultValue = 0)
  val LimitArgument  = Argument("limit", OptionInputType(IntType), defaultValue = 100)

  def apply[F[_]: Async](effectDispatcher: Dispatcher[F]): ObjectType[HeadlinesStore[F], Unit] =
    ObjectType(
      name = "Query",
      fields = fields(
        Field(
          name = "news",
          fieldType = ListType(NewsType()),
          arguments = OffsetArgument :: LimitArgument :: Nil,
          description = Some("The New York Times headlines"),
          resolve = c => c.withArgs(OffsetArgument, LimitArgument)((offset, limit) =>
            Offset.validator.validate(offset) |+| Limit.validator.validate(limit) match {
              case None => effectDispatcher.unsafeToFuture(c.ctx.fetch(offset, limit))
              case Some(errors) => effectDispatcher.unsafeToFuture(Async[F].raiseError(new BadArgumentException(errors)))
            }))
      ))
}
