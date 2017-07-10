import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes._
import models._
import org.scalatest.concurrent.ScalaFutures
import scala.language.postfixOps
import spray.json._

class MoviesRouteSpec extends BaseServiceSpec with ScalaFutures {

  def actorRefFactory = system

  import services.Fixtures._

  "Movies Routes" should {

    "return list of all movies" in {
      Get("/v1/movies") ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[Seq[Movie]] should equal(testMovies)
      }
    }

    "return list of movies by querystring imdbId" in {
      Get(s"/v1/movies?imdbId=${testMovies.head.imdbId}", HttpEntity("application/json")) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[Option[Movie]] should equal(Some(testMovies.head))
      }
    }

    "create a movie" in {
      val movie = Movie(imdbId = "tt1", movieTitle = Some("movieTitle"))
      Post(s"/v1/movies", movie.toJson) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual Created
        val response = responseAs[Movie]
        response.imdbId should equal(movie.imdbId)
        response.movieTitle should equal(movie.movieTitle)

        whenReady(moviesService.getMovies) {
          result => result.size should equal(testMovies.size + 1)
        }
      }
    }

    "update a movie" in {
      val movieWithNewTitle = testMovies.head.copy(movieTitle = Some("new movie title"))
      Put(s"/v1/movies", movieWithNewTitle.toJson) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        val response = responseAs[Movie]
        response.movieTitle should equal(movieWithNewTitle.movieTitle)
      }
    }

    "delete a movie" in {
      val movieImdbToDelete = testMovies.head.imdbId
      Delete(s"/v1/movies", movieImdbToDelete) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual NoContent
        whenReady(moviesService.findByImdbId(movieImdbToDelete)) {
          result => result should be(None: Option[Movie])
        }
      }
    }
  }
}