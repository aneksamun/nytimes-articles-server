package co.uk.redpixel.articles.data

import cats.data.NonEmptyList
import co.uk.redpixel.articles.data.Validator.FieldError

final case class Limit(value: Int) extends AnyVal

object Limit {

  val validator: Validator[Limit] = (limit: Limit) =>
    Option.when(limit.value < 1)(NonEmptyList.of(FieldError("limit", "Must be greater than 0")))

  implicit def intToLimit(value: Int): Limit = Limit(value)
}
