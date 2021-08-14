package co.uk.redpixel.articles

import cats.effect.Async
import cats.syntax.all._
import co.uk.redpixel.articles.algebra.GraphQL
import io.circe.optics.JsonPath.root
import io.circe.{Json, JsonObject}
import sangria.ast.Document
import sangria.execution.{ExceptionHandler, Executor, HandledException, WithViolations}
import _root_.sangria.marshalling.circe._
import sangria.parser.{QueryParser, SyntaxError}
import sangria.schema.Schema
import sangria.validation.AstNodeViolation

import scala.util.{Failure, Success}

object SangriaGraphQL {

  private val queryStringLens   = root.query.string
  private val operationNameLens = root.operationName.string
  private val variablesLens     = root.variables.obj

  // Format a SyntaxError as a GraphQL `errors`
  private def formatSyntaxError(e: SyntaxError): Json = Json.obj(
    "errors" -> Json.arr(Json.obj(
      "message" -> Json.fromString(e.getMessage),
      "locations" -> Json.arr(Json.obj(
        "line" -> Json.fromInt(e.originalError.position.line),
        "column" -> Json.fromInt(e.originalError.position.column))))))

  // Format a WithViolations as a GraphQL `errors`
  private def formatWithViolations(e: WithViolations): Json = Json.obj(
    "errors" -> Json.fromValues(e.violations.map {
      case v: AstNodeViolation => Json.obj(
        "message" -> Json.fromString(v.errorMessage),
        "locations" -> Json.fromValues(v.locations.map(loc => Json.obj(
          "line" -> Json.fromInt(loc.line),
          "column" -> Json.fromInt(loc.column)))))
      case v =>
        Json.obj("message" -> Json.fromString(v.errorMessage))
    }))

  // Format a String as a GraphQL `errors`
  private def formatString(s: String): Json = Json.obj(
    "errors" -> Json.arr(Json.obj(
      "message" -> Json.fromString(s))))

  // Format a Throwable as a GraphQL `errors`
  private def formatThrowable(e: Throwable): Json = Json.obj(
    "errors" -> Json.arr(Json.obj(
      "class" -> Json.fromString(e.getClass.getName),
      "message" -> Json.fromString(e.getMessage))))


  def apply[F[_]] = new Partial[F]

  final class Partial[F[_]] {

    def apply[A](schema: Schema[A, Unit], context: A)(implicit F: Async[F]): GraphQL[F] =
      new GraphQL[F] {

        def liftError(json: Json): F[Either[Json, Json]] =
          F.pure(json.asLeft)

        // Destructure `request`
        def query(request: Json): F[Either[Json, Json]] = {
          val queryString   = queryStringLens.getOption(request)
          val operationName = operationNameLens.getOption(request)
          val variables     = variablesLens.getOption(request).getOrElse(JsonObject())

          queryString match {
            case Some(qs) => query(qs, operationName, variables)
            case None => liftError(formatString("No 'query' property was present in the request."))
          }
        }

        // Parse `query` and execute
        def query(query: String, operationName: Option[String], variables: JsonObject): F[Either[Json, Json]] =
          QueryParser.parse(query) match {
            case Success(ast) => execute(schema, context, ast, operationName, variables)
            case Failure(e@SyntaxError(_, _, _)) => liftError(formatSyntaxError(e))
            case Failure(e) => liftError(formatThrowable(e))
          }

        // Execute a GraphQL query with Sangria
        def execute(schema: Schema[A, Unit],
                    context: A,
                    query: Document,
                    operationName: Option[String],
                    variables: JsonObject): F[Either[Json, Json]] = {
          F.executionContext.flatMap { implicit ec =>
            F.async_ { cb: (Either[Throwable, Json] => Unit) =>
              Executor.execute(
                schema = schema,
                queryAst = query,
                userContext = context,
                variables = Json.fromJsonObject(variables),
                operationName = operationName,
                exceptionHandler = ExceptionHandler {
                  case (_, e) => HandledException(e.getMessage)
                }
              ).onComplete {
                case Success(value) => cb(Right(value))
                case Failure(error) => cb(Left(error))
              }
            }
          }.attempt.flatMap {
            case Right(json) => F.pure(json.asRight)
            case Left(err: WithViolations) => liftError(formatWithViolations(err))
            case Left(err) => liftError(formatThrowable(err))
          }
        }
      }
  }
}
