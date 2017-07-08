import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import api.ApiService
import com.typesafe.scalalogging.StrictLogging
import services.{DatabaseServiceImpl, FlywayServiceImpl}
import utils.{ActorContext, Config}

object Main extends App with Config with ActorContext with StrictLogging {

  val databaseService = new DatabaseServiceImpl(dbUrl, dbUser, dbPass)
  val flywayService = new FlywayServiceImpl(dbUrl, dbUser, dbPass)
  logger.info("Running database migrations...")
  val dbMigrationResult = flywayService.migrate
  logger.info(s"Database migration result: $dbMigrationResult")
  val apiService = new ApiService()

  Http().bindAndHandle(handler = logRequestResult("log")(apiService.routes), interface = httpInterface, port = httpPort)
}