package api

import akka.http.scaladsl.server.Directives._
import api.routes.{MovieShowingsRoute, MoviesRoute}
import services.{MovieShowingsService, MoviesService}

import scala.concurrent.ExecutionContext

class ApiService(moviesService: MoviesService,
                 movieShowingsService: MovieShowingsService)(implicit executionContext: ExecutionContext) {

  val moviesRoute = new MoviesRoute(moviesService)
  val movieShowingsRoute = new MovieShowingsRoute(movieShowingsService)
  val routes = pathPrefix("v1") {
    moviesRoute.route ~
      movieShowingsRoute.route
  }

}