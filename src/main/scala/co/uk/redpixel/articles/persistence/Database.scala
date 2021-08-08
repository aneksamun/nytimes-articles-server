package co.uk.redpixel.articles.persistence

import cats.Monad
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxOptionId}
import co.uk.redpixel.articles.config.DatabaseConfig
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult

import scala.util.Try

object Database {

  type MigrationResult = Either[Throwable, MigrateResult]

  def createSchema[F[_]](config: DatabaseConfig)
                        (xa: HikariTransactor[F])
                        (implicit F: Monad[F]): F[Option[MigrationResult]] = {
    def migrateSchema() = {
      xa.configure { dataSource =>
        Try(Flyway.configure()
          .dataSource(dataSource)
          .load()
          .migrate()).toEither.some.pure[F]
      }
    }

    Monad[F].ifM(F.pure(config.whetherCreateSchema))(migrateSchema(), F.pure(None))
  }
}
