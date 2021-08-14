package co.uk.redpixel.articles.data

import cats.Show
import cats.data.NonEmptyList
import co.uk.redpixel.articles.data.Validator.ValidationResult

trait Validator[T] {
  def validate(target: T): ValidationResult
}

object Validator {

  type FieldName = String
  type Message = String

  final case class FieldError(fieldName: FieldName, message: Message)

  implicit val showFieldError: Show[FieldError] = (error: FieldError) =>
    s"${error.fieldName}: ${error.message}"

  type ValidationResult = Option[NonEmptyList[FieldError]]
}
