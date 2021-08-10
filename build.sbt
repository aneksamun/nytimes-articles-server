val Http4sVersion = "0.23.0"
val CirceVersion = "0.14.1"
val PureConfigVersion = "0.16.0"
val PureConfigRefinedVersion = "0.9.27"
val LogbackVersion = "1.2.5"
val FlywayVersion = "7.12.1"
val PostgresSqlDriverVersion = "42.2.23"
val QuillJdbcVersion = "3.9.0"
val KindProjectorVersion = "0.13.0"
val BetterMonadicVersion = "0.3.1"

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
      "io.getquill"           %% "quill-jdbc"          % QuillJdbcVersion,
      "io.circe"              %% "circe-generic"       % CirceVersion,
      "com.github.pureconfig" %% "pureconfig"          % PureConfigVersion,
      "eu.timepit"            %% "refined-pureconfig"  % PureConfigRefinedVersion,
      "ch.qos.logback"        %  "logback-classic"     % LogbackVersion,
      "org.postgresql"        %  "postgresql"          % PostgresSqlDriverVersion,
      "org.flywaydb"          %  "flyway-core"         % FlywayVersion
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
