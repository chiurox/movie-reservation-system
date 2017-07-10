package api.routes

import akka.http.scaladsl.server.Route
import models.{ImdbId, Movie}
import services.MoviesService

import scala.concurrent.ExecutionContext
import scala.language.postfixOps

class MoviesRoute(moviesService: MoviesService)(implicit executionContext: ExecutionContext) extends BaseRoute {

  import akka.http.scaladsl.model.StatusCodes._
  import moviesService._

  val route: Route = pathPrefix("movies") {
    pathEndOrSingleSlash {
      post {
        entity(as[Movie]) { movie =>
          complete(Created -> insert(movie))
        }
      } ~
        put {
          entity(as[Movie]) { movie =>
            complete(update(movie))
          }
        } ~
        delete {
          entity(as[ImdbId]) { imdbId =>
            onSuccess(moviesService.delete(imdbId)) { _ =>
              complete(NoContent)
            }
          }
        } ~
        parameters('imdbId.as[ImdbId].?) {
          case Some(imdbId) => complete(findByImdbId(imdbId))
          case None => complete(getMovies)
        }
    }
  }

}
