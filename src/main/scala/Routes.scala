import akka.http.scaladsl.server.Directives._

trait Routes {
  val routes =
    pathPrefix("v1") (getFromResource("public/index.html"))
}
