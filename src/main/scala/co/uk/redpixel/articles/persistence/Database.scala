package co.uk.redpixel.articles.persistence

import cats.effect.{Resource, Sync}
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxOptionId}
import cats.{Applicative, Monad}
import co.uk.redpixel.articles.config.DatabaseConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import javax.sql.DataSource
import scala.util.Try

object Database {

  type MigrationResult = Either[Throwable, MigrateResult]

  def createSchema[F[_]](config: DatabaseConfig)
                        (dataSource: DataSource)
                        (implicit F: Monad[F]): F[Option[MigrationResult]] = {
    def migrateSchema() =
      Try(Flyway.configure()
        .dataSource(dataSource)
        .load()
        .migrate()).toEither.some.pure[F]

    Monad[F].ifM(F.pure(config.whetherCreateSchema))(migrateSchema(), F.pure(None))
  }

  def createSource[F[_] : Sync](config: DatabaseConfig): Resource[F, HikariDataSource] = {
    Resource.fromAutoCloseable(Applicative[F].pure(
      HikariConnectionPool.builder
        .withUser(config.user)
        .withPassword(config.password)
        .withDriverClassName(config.driverClassName)
        .withJdbcUrl(config.jdbcUrl)
        .build
    ))
  }
}
