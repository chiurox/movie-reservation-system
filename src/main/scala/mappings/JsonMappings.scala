package mappings

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.{Movie, MovieShowing, MovieShowingRegistration}
import spray.json.DefaultJsonProtocol

trait JsonMappings extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val movieFormat = jsonFormat3(Movie)
  implicit val movieShowing = jsonFormat5(MovieShowing)
  implicit val movieShowingRegistration = jsonFormat3(MovieShowingRegistration)
}