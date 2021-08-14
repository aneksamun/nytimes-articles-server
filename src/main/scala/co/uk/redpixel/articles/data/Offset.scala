package co.uk.redpixel.articles.data

import cats.data.NonEmptyList
import co.uk.redpixel.articles.data.Validator.FieldError

final case class Offset(value: Int) extends AnyVal

object Offset {

  val validator: Validator[Offset] = (offset: Offset) =>
    Option.when(offset.value < 0)(NonEmptyList.of(FieldError("offset", "Must be non negative")))

  implicit def intToOffset(value: Int): Offset = Offset(value)
}
