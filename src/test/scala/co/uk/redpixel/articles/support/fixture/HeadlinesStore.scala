package co.uk.redpixel.articles.support.fixture

import cats.effect.IO
import co.uk.redpixel.articles.algebra.HeadlineStore
import co.uk.redpixel.articles.persistence.QuillHeadlineStore
import co.uk.redpixel.articles.support.fixture.HeadlinesStore._
import com.typesafe.config.{ConfigFactory, ConfigValue, ConfigValueFactory}

trait HeadlinesStore {
  this: ServerDependencies =>

  lazy val headlinesStore: HeadlineStore[IO] =
    QuillHeadlineStore[IO](ConfigFactory.empty()
      .withValue("database", database.databaseName)
      .withValue("user", database.username)
      .withValue("password", database.password)
      .withValue("host", database.containerIpAddress)
      .withValue("port", database.mappedPort(5432))
    )
}

object HeadlinesStore {

  implicit def valueToConfigValue[T](value: T): ConfigValue =
    ConfigValueFactory.fromAnyRef(value)
}
