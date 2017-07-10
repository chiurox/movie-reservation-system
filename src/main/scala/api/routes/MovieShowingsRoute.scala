package api.routes

import akka.http.scaladsl.server.Route
import models._
import services.MovieShowingsService

import scala.concurrent.ExecutionContext
import scala.language.postfixOps

class MovieShowingsRoute(movieShowingsService: MovieShowingsService)(implicit executionContext: ExecutionContext) extends BaseRoute {

  import movieShowingsService._
  import akka.http.scaladsl.model.StatusCodes._

  val route: Route = pathPrefix("movie-showings") {
    pathEndOrSingleSlash {
      put {
        entity(as[MovieShowingReservation]) { reservation =>
          onSuccess(reserveSeat(reservation)) {
            case SeatsReservedSuccessfully => complete(OK, s"${SeatsReservedSuccessfully.message}: $reservation")
            case NoSeatsAvailable => complete(Conflict, s"${NoSeatsAvailable.message}: $reservation")
            case MovieShowingNotFound => complete(NotFound, s"${MovieShowingNotFound.message}: $reservation")
          }
        }
      } ~
        post {
          entity(as[MovieShowingRegistration]) { showing =>
            complete(Created -> insert(showing))
          }
        } ~
        parameters('imdbId.as[ImdbId], 'screenId.as[ScreenId]) {
          (imdbId, screenId) => complete(getMovieInformationByIdentifiers(imdbId, screenId))
        } ~
        get {
          complete(getShowings)
        }
    }
  }

}
