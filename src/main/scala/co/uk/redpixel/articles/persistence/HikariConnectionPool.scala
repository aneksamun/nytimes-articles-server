package co.uk.redpixel.articles.persistence

import co.uk.redpixel.articles.config.DatabaseConfig.{DriverClassName, Password, UserName}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import eu.timepit.refined.auto._

import java.net.URI

object HikariConnectionPool {

  def builder: Builder = Builder(new HikariConfig())

  final case class Builder(config: HikariConfig) {

    def withDriverClassName(driverClassName: DriverClassName): Builder = {
      config setDriverClassName driverClassName
      copy(config)
    }

    def withJdbcUrl(jdbcUrl: URI): Builder = {
      config setJdbcUrl jdbcUrl.toString
      copy(config)
    }

    def withUser(user: UserName): Builder = {
      config setUsername user
      copy(config)
    }

    def withPassword(password: Password): Builder = {
      config setPassword password
      copy(config)
    }

    def build = new HikariDataSource(config)
  }
}
