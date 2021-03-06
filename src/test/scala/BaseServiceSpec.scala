import akka.http.scaladsl.testkit.ScalatestRouteTest
import api.ApiService
import com.typesafe.scalalogging.StrictLogging
import mappings.JsonMappings
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import services._
import utils.Config

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseServiceSpec extends WordSpec
  with Matchers
  with ScalatestRouteTest
  with Mockito
  with Config
  with JsonMappings
  with StrictLogging {
  val dbTestService = new DatabaseServiceImpl(dbUrl, dbUser, dbPass)
  import dbTestService._
  import driver.api._
  val flywayService = new FlywayServiceImpl(dbUrl, dbUser, dbPass)


  val movieTitlesService: MovieTitlesService = mock[MovieTitlesService]
  val moviesService = new MoviesServiceImpl(dbTestService, movieTitlesService)
  val movieShowingsService = new MovieShowingsServiceImpl(dbTestService, moviesService)
  val apiService = new ApiService(moviesService, movieShowingsService)
  val routes = apiService.routes

  logger.info(s"$dbUrl, $dbUser")
  logger.info("Running database migrations for test...")
  val dbMigrationResult = flywayService.reload()
  logger.info(s"Database migrations run: $dbMigrationResult")

  Await.result(db.run(moviesService.getTable ++= Fixtures.testMovies), 10.seconds)
  Await.result(db.run(movieShowingsService.getTable ++= Fixtures.testMovieShowings), 10.seconds)

}