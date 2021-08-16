val Http4sVersion = "0.23.0"
val CirceVersion = "0.14.1"
val PureConfigVersion = "0.16.0"
val PureConfigRefinedVersion = "0.9.27"
val LogbackVersion = "1.2.5"
val FlywayVersion = "7.12.1"
val PostgresVersion = "42.2.23"
val QuillVersion = "3.9.0"
val KindProjectorVersion = "0.13.0"
val BetterMonadicVersion = "0.3.1"
val SangriaVersion = "2.1.3"
val SangriaCirceVersion = "1.3.2"
val ScalaScraperVersion = "2.2.1"
val ScalaTestVersion = "3.2.9"
val ScalaTestScalaCheckVersion = "3.1.0.0-RC2"
val TestContainerVersion = "0.39.5"
val MockServerClientVersion = "5.11.2"

lazy val root = (project in file("."))
  .settings(
    organization := "co.uk.redpixel",
    name := "nytimes-articles-server",
    version := "1.0.0",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "org.http4s"             %% "http4s-ember-server"             % Http4sVersion,
      "org.http4s"             %% "http4s-circe"                    % Http4sVersion,
      "org.http4s"             %% "http4s-dsl"                      % Http4sVersion,
      "io.getquill"            %% "quill-async-postgres"            % QuillVersion,
      "io.circe"               %% "circe-generic"                   % CirceVersion,
      "io.circe"               %% "circe-optics"                    % CirceVersion,
      "com.github.pureconfig"  %% "pureconfig"                      % PureConfigVersion,
      "eu.timepit"             %% "refined-pureconfig"              % PureConfigRefinedVersion,
      "org.sangria-graphql"    %% "sangria"                         % SangriaVersion,
      "org.sangria-graphql"    %% "sangria-circe"                   % SangriaCirceVersion,
      "net.ruippeixotog"       %% "scala-scraper"                   % ScalaScraperVersion,
      "org.scalatest"          %% "scalatest"                       % ScalaTestVersion           % Test,
      "org.scalatestplus"      %% "scalatestplus-scalacheck"        % ScalaTestScalaCheckVersion % Test,
      "com.dimafeng"           %% "testcontainers-scala-scalatest"  % TestContainerVersion       % Test,
      "com.dimafeng"           %% "testcontainers-scala-postgresql" % TestContainerVersion       % Test,
      "com.dimafeng"           %% "testcontainers-scala-mockserver" % TestContainerVersion       % Test,
      "org.mock-server"        %  "mockserver-client-java"          % MockServerClientVersion    % Test,
      "ch.qos.logback"         %  "logback-classic"                 % LogbackVersion,
      "org.flywaydb"           %  "flyway-core"                     % FlywayVersion,
      "org.postgresql"         %  "postgresql"                      % PostgresVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % KindProjectorVersion cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % BetterMonadicVersion),
    assembly / mainClass := Some("co.uk.redpixel.articles.Main")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-explaintypes",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:existentials",
  "-feature",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-extra-implicit",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:privates",
  "-Ywarn-value-discard"
)
