package api

import akka.http.scaladsl.server.Directives._
import api.routes.MoviesRoute
import services.MoviesService

import scala.concurrent.ExecutionContext

class ApiService(moviesService: MoviesService)(implicit executionContext: ExecutionContext) {

  val moviesRoute = new MoviesRoute(moviesService)
  val routes = pathPrefix("v1") {
    moviesRoute.route
  }

}