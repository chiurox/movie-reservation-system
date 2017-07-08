package api

import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext

class ApiService()(implicit executionContext: ExecutionContext) {

  val routes = pathPrefix("v1") (getFromResource("public/index.html"))

}