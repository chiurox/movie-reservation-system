import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes._
import models.Movie
import org.scalatest.concurrent.ScalaFutures

class MoviesRouteSpec extends BaseServiceSpec with ScalaFutures {

  def actorRefFactory = system

  import services.Fixtures._
  "Movies Routes" should {

    "return list of all movies" in {
      Get("/v1/movies", HttpEntity("application/json")) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[Seq[Movie]] should equal(testMovies)
      }
    }

    "return list of movies by querystring imdbId" in {
      Get(s"/v1/movies?imdbId=${testMovies.head.imdbId}", HttpEntity("application/json")) ~> routes ~> check {
        handled shouldEqual true
        status shouldEqual OK
        responseAs[Seq[Movie]] should equal(Seq(testMovies.head))
      }
    }
  }
}