package co.uk.redpixel.articles.support.fixture

import co.uk.redpixel.articles.support.Resources
import co.uk.redpixel.articles.support.fixture.ServerDependencies.Database
import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import com.dimafeng.testcontainers.{MockServerContainer, PostgreSQLContainer}
import org.flywaydb.core.Flyway
import org.scalatest.Suite
import org.testcontainers.utility.DockerImageName
import scalaz.Scalaz.ToIdOps

trait ServerDependencies extends TestContainersForAll with Resources {
  this: Suite =>

  lazy val database: PostgreSQLContainer = withContainers(_.head)
  lazy val serverMock: MockServerContainer = withContainers(_.tail)

  override type Containers = PostgreSQLContainer and MockServerContainer

  override def startContainers(): Containers =
    PostgreSQLContainer.Def(
      dockerImageName = DockerImageName.parse("postgres:13.3-alpine"),
      databaseName = Database.name,
      username = Database.user,
      password = Database.password
    ).start() and MockServerContainer.Def("5.8.1").start()

  override def afterContainersStart(containers: PostgreSQLContainer and MockServerContainer): Unit =
    Flyway.configure()
      .dataSource(containers.head.jdbcUrl, containers.head.username, containers.head.password)
      .load()
      .migrate()
      .migrationsExecuted
      .toString |> "Migration scripts executed: ".concat |> println
}

object ServerDependencies {

  object Database {
    val name = "nytimes-articles"
    val user = "user"
    val password = "1234"
  }
}
