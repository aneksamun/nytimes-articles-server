package co.uk.redpixel.articles.schema

import cats.data.NonEmptyList
import cats.implicits.toShow
import co.uk.redpixel.articles.data.Validator

final class BadArgumentException(errors: NonEmptyList[Validator.FieldError])
  extends Exception(
    errors.map(_.show).toList.mkString("; "))
