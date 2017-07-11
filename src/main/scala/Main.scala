import akka.http.scaladsl.Http
import api.{ApiService, Resource}
import com.typesafe.scalalogging.StrictLogging
import services._
import utils.{ActorContext, Config}

object Main extends App with Config with ActorContext with Resource with StrictLogging {

  val dbService = new DatabaseServiceImpl(dbUrl, dbUser, dbPass)
  val flywayService = new FlywayServiceImpl(dbUrl, dbUser, dbPass)
  logger.info("Running database migrations...")
  val dbMigrationResult = flywayService.migrate
  logger.info(s"Database migrations run: $dbMigrationResult")

  val movieTitlesService = new MovieTitlesServiceImpl(movieTitleEndpoint, movieTitleToken)
  val moviesService = new MoviesServiceImpl(dbService, movieTitlesService)
  val movieShowingsService = new MovieShowingsServiceImpl(dbService, moviesService)
  val apiService = new ApiService(moviesService, movieShowingsService)

  Http().bindAndHandle(handler = logRequestResult("log")(apiService.routes), interface = httpInterface, port = httpPort)
}