# The New York Times articles server

![Build status](https://github.com/aneksamun/nytimes-articles-server/actions/workflows/scala.yml/badge.svg)

The service scrapes all news headlines from [nytimes.com](https://www.nytimes.com/) and expose them using the GraphQL API.   
The articles can be queried using following GraphQL schema:

```
type News {
  title: String,
  link: String,
}

type Query {
  news: [News!]!
}
```

Once service is started it will start scraping headlines and will redirect user to [GraphQL Playground](https://github.com/graphql/graphql-playground) page.

![playground](doc/playground.png?raw=true "Playground")

#### Configuration

The service can customised by changing following settings

| Setting                  | Description                                            | Default value            |
|--------------------------|--------------------------------------------------------|--------------------------|
| SERVER_HTTP_PORT         | Server port                                            | 8080                     |
| DB_NAME                  | Database name                                          | wardrobe                 |
| DB_HOST                  | Database server host                                   | localhost                |
| DB_PORT                  | Database server port                                   | 5432                     |
| DB_USER                  | Database user                                          | user                     |
| DB_PASSWORD              | Database user password                                 | 1234                     |
| DB_WHETHER_CREATE_SCHEMA | Whether to create a database schema on the system run? | true                     |
| NY_TIMES_URL             | The URL of the New York Times website                  | https://www.nytimes.com/ |
| SCRAPE_REPEAT_INTERVAL   | Scrape repeating interval                              | every 4 hours            |

### How to build?

- Clone project
- Build the project
- Run tests
```
sbt compile
sbt test
```

### Technology stack
- [scala 2.13.6](http://www.scala-lang.org/) as the main application programming language
- [http4s](https://http4s.org/) typeful, functional, streaming HTTP for Scala
- [sangria](https://sangria-graphql.github.io/) a GraphQL implementation for Scala
- [scala-scraper](https://github.com/ruippeixotog/scala-scraper) a Scala library for scraping content from HTML pages
- [quill](https://getquill.io/) compile-time language integrated queries for Scala
- [cats](http://typelevel.org/cats/) to write more functional and less boilerplate code
- [cats-effect](https://github.com/typelevel/cats-effect) The Haskell IO monad for Scala
- [pureconfig](https://pureconfig.github.io/) for loading configuration files
- [refined](https://github.com/fthomas/refined) for type constraints avoiding unnecessary testing and boilerplate
- [circe](https://circe.github.io/circe/) a JSON library for Scala
- [scalatest](http://www.scalatest.org/) and [ScalaCheck](https://www.scalacheck.org/) for *unit and property based testing*
- [testcontainers](https://github.com/testcontainers/testcontainers-scala) to run system dependant services for Integration Testing purposes
