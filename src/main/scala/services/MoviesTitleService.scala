package services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import com.typesafe.scalalogging.StrictLogging
import models._
import spray.json._
import utils.ActorContext

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

trait MovieTitlesService {

  def fetchTitle(imdbId: ImdbId): Future[Option[String]]

}

class MovieTitlesServiceImpl(val endpoint: String, val token: String)
  extends MovieTitlesService with ActorContext with StrictLogging {

  override def fetchTitle(imdbId: ImdbId): Future[Option[String]] = {
    val uri = s"${endpoint.replace("<imdbId>", imdbId).replace("<token>", token)}"
    logger.debug(s"Requesting movie title for imdbId: $imdbId to: $uri ")
    Http().singleRequest(HttpRequest(uri = uri)) flatMap {
      resp =>
        resp.entity.toStrict(15 seconds)
        .map(_.data.decodeString("UTF-8"))
        .map { r =>
          val jsonResponse = r.stripMargin.parseJson.asJsObject.fields("data").asJsObject.fields("movies").toString
          logger.debug(s"JSON response from API call: $jsonResponse")
          val startOfTitle = jsonResponse.indexOf("\"title\":")
          val afterTitle =  jsonResponse.substring(startOfTitle + 9)
          val endOfTitle = afterTitle.indexOf("\",")
          afterTitle.substring(0, endOfTitle)
        }.map(Some(_))
    }
  }
}

