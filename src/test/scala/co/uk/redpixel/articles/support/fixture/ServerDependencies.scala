package co.uk.redpixel.articles.support.fixture

import co.uk.redpixel.articles.support.Resources
import co.uk.redpixel.articles.support.fixture.ServerDependencies.Configuration
import co.uk.redpixel.articles.support.fixture.ServerDependencies.Configuration._
import com.dimafeng.testcontainers.lifecycle.and
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import com.dimafeng.testcontainers.{MockServerContainer, PostgreSQLContainer}
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.Suite
import org.testcontainers.utility.DockerImageName

trait ServerDependencies extends TestContainersForAll with Resources {
  this: Suite =>

  override type Containers = PostgreSQLContainer and MockServerContainer

  override def startContainers(): Containers =
    PostgreSQLContainer.Def(
      dockerImageName = DockerImageName.parse("postgres:13.3-alpine"),
      databaseName = Configuration.db.name,
      username = Configuration.db.username,
      password = Configuration.db.password
    ).start() and MockServerContainer.Def("5.8.1").start()

  override def afterContainersStart(containers: Containers): Unit =
    onceStarted(containers.tail)

  override def beforeContainersStop(containers: PostgreSQLContainer and MockServerContainer): Unit =
    onShutdown()

  def onceStarted(container: MockServerContainer): Unit

  def onShutdown(): Unit
}

object ServerDependencies {

  object Configuration {

    val db: Config = ConfigFactory.load.getConfig("db")

    implicit class DatabaseConfigOps(val config: Config) extends AnyVal {
      def name: String = db.getString("database")
      def username: String = db.getString("user")
      def password: String = db.getString("password")
    }
  }
}
