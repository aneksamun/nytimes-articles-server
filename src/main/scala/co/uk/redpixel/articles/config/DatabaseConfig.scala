package co.uk.redpixel.articles.config

import co.uk.redpixel.articles.config.DatabaseConfig.{Password, UserName}
import eu.timepit.refined.types.all.NonEmptyString

import java.net.URI

final case class DatabaseConfig(jdbcUrl: URI,
                                user: UserName,
                                password: Password,
                                whetherCreateSchema: Boolean)

object DatabaseConfig {

  type UserName = NonEmptyString
  type Password = NonEmptyString
}
