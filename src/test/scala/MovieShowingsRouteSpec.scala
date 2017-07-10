import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import models._
import org.scalatest.concurrent.ScalaFutures
import spray.json._

class MovieShowingsRouteSpec extends BaseServiceSpec with ScalaFutures {

  def actorRefFactory = system

  import services.Fixtures._

  "Movie Showings Routes" should {

    "return list of all movies showing" in {
      Get("/v1/movie-showings") ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[Seq[MovieInformation]] should equal(testMovieInformations)
      }
    }

    "return movie informations by querydtring params imdbId and screenId" in {
      Get(s"/v1/movie-showings?imdbId=${testMovieShowings.last.imdbId}&screenId=${testMovieShowings.last.screenId}") ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[Option[MovieInformation]] should equal(Some(testMovieInformations.last))
      }
    }

    "return not found for inexistent imdbId and screenId" in {
      Get(s"/v1/movie-showings?imdbId=NOT_IN_DATABASE&screenId=CANT_FIND_ME") ~>  Route.seal(routes) ~> check {
        status shouldEqual NotFound
      }
    }

    "register a movie showing" in {
      val newMovieShowingRegistration = MovieShowingRegistration(imdbId = "t11", screenId = "s1", availableSeats = 100)
      Post(s"/v1/movie-showings", newMovieShowingRegistration.toJson) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual Created
        val response = responseAs[MovieInformation]
        response.availableSeats should equal(newMovieShowingRegistration.availableSeats)
        response.imdbId should equal(newMovieShowingRegistration.imdbId)
        response.screenId should equal(newMovieShowingRegistration.screenId)

        whenReady(movieShowingsService.getShowings) {
          result => result.size should equal(testMovieInformations.size + 1)
        }
      }
    }
    "reserve a seat at a movie showing" in {
      val movieShowingToReserve = testMovieShowings.last
      val newMovieShowingReservation = MovieShowingReservation(imdbId = movieShowingToReserve.imdbId, screenId = movieShowingToReserve.screenId)
      Put(s"/v1/movie-showings", newMovieShowingReservation.toJson) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[String] should equal(s"${SeatsReservedSuccessfully.message}: $newMovieShowingReservation")
        whenReady(movieShowingsService.findMovieShowingByIdentifiers(movieShowingToReserve.imdbId, movieShowingToReserve.screenId)) {
          case Some(movieShowing) => movieShowing.reservedSeats should equal(movieShowingToReserve.reservedSeats + 1)
          case _ => fail
        }
      }
    }

    "not reserve a seat if movie showing is beyond capacity" in {
      val movieShowingToReserve = testMovieShowings.head
      val newMovieShowingReservation = MovieShowingReservation(
        imdbId = movieShowingToReserve.imdbId,
        screenId = movieShowingToReserve.screenId,
        seatsToReserve = Some(10000)
      )
      Put(s"/v1/movie-showings", newMovieShowingReservation.toJson) ~> routes ~> check {
        handled shouldEqual true
        responseAs[String] should equal(s"${NoSeatsAvailable.message}: $newMovieShowingReservation")
        whenReady(movieShowingsService.findMovieShowingByIdentifiers(movieShowingToReserve.imdbId, movieShowingToReserve.screenId)) {
          case Some(movieShowing) => movieShowing.reservedSeats should equal(movieShowingToReserve.reservedSeats)
          case _ => fail
        }
      }
    }

    "not reserve a seat if movie showing can't be found by imdbId + screenId" in {
      val fakeMovieShowingReservation = MovieShowingReservation(
        imdbId = "fake_imdb_id",
        screenId = "fake_screen_id",
        seatsToReserve = Some(10000)
      )
      Put(s"/v1/movie-showings", fakeMovieShowingReservation.toJson) ~> routes ~> check {
        handled shouldEqual true
        responseAs[String] should equal(s"${MovieShowingNotFound.message}: $fakeMovieShowingReservation")
        whenReady(movieShowingsService.findMovieShowingByIdentifiers(fakeMovieShowingReservation.imdbId, fakeMovieShowingReservation.screenId)) {
          case None => true
          case _ => fail
        }
      }
    }

  }
}