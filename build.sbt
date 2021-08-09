val Http4sVersion = "0.23.0"
val DoobieVersion = "1.0.0-M5"
val CirceVersion = "0.14.1"
val PureConfigVersion = "0.16.0"
val PureConfigRefinedVersion = "0.9.27"
val LogbackVersion = "1.2.5"
val FlywayVersion = "7.12.1"

lazy val root = (project in file("."))
  .settings(
    organization := "co.uk.redpixel",
    name := "nytimes-articles-server",
    version := "1.0.0",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"            %% "http4s-circe"        % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"          % Http4sVersion,
      "org.tpolecat"          %% "doobie-core"         % DoobieVersion,
      "org.tpolecat"          %% "doobie-postgres"     % DoobieVersion,
      "org.tpolecat"          %% "doobie-hikari"       % DoobieVersion,
      "org.tpolecat"          %% "doobie-quill"        % DoobieVersion,
      "io.circe"              %% "circe-generic"       % CirceVersion,
      "com.github.pureconfig" %% "pureconfig"          % PureConfigVersion,
      "eu.timepit"            %% "refined-pureconfig"  % PureConfigRefinedVersion,
      "ch.qos.logback"        %  "logback-classic"     % LogbackVersion,
      "org.flywaydb"          %  "flyway-core"         % FlywayVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
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
