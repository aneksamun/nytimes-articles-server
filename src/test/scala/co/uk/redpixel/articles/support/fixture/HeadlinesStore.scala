package co.uk.redpixel.articles.support.fixture

import cats.effect.IO
import co.uk.redpixel.articles.algebra.HeadlineStore
import co.uk.redpixel.articles.persistence.QuillHeadlineStore

trait HeadlinesStore {
  this: ServerDependencies =>

  lazy val headlinesStore: HeadlineStore[IO] =
    QuillHeadlineStore[IO]()
}
