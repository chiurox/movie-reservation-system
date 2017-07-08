import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.StrictLogging
import utils.{ActorContext, Config}

object Main extends App with Config with Routes with ActorContext with StrictLogging {
  Http().bindAndHandle(handler = logRequestResult("log")(routes), interface = httpInterface, port = httpPort)
}