package api.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import mappings.JsonMappings
import models.ImdbId
import services.MoviesService

import scala.concurrent.ExecutionContext
import scala.language.postfixOps

class MoviesRoute(moviesService: MoviesService)(implicit executionContext: ExecutionContext) extends JsonMappings {

  import moviesService._

  val route: Route = pathPrefix("movies") {
    pathEndOrSingleSlash {
      parameters('imdbId.as[ImdbId].?) {
        case Some(imdbId) => complete(findByImdbId(imdbId))
        case None => complete(getMovies)
      }
    }
  }
}
