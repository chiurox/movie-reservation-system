package services

import com.typesafe.scalalogging.StrictLogging
import models._
import models.db.{MovieShowingTable, MovieTable}
import utils.ActorContext

import scala.concurrent.Future
import scala.language.postfixOps

trait MovieShowingsService {

  def getShowings: Future[Seq[MovieInformation]]

  def findMovieShowingByIdentifiers(imdbId: ImdbId, screenId: ScreenId): Future[Option[MovieShowing]]

  def getMovieInformationByIdentifiers(imdbId: ImdbId, screenId: ScreenId): Future[Option[MovieInformation]]

  def reserveSeat(movieShowingReservation: MovieShowingReservation): Future[ServiceResult]

  def insert(movie: MovieShowingRegistration): Future[ServiceResult]
}

class MovieShowingsServiceImpl(val databaseService: DatabaseService,
                               val moviesService: MoviesService)
  extends MovieShowingTable with MovieShowingsService with MovieTable with ActorContext with StrictLogging {

  import databaseService._
  import databaseService.driver.api._

  override def getShowings: Future[Seq[MovieInformation]] = {
    db.run((for {
      (movies, movieShowings) <- movies join moviesShowing on (_.imdbId === _.imdbId)
    } yield (movies, movieShowings)).result).map(_.map {
      case (movie, movieShowing) => movieShowing.toMovieInformation(movie.movieTitle)
    })
  }

  override def findMovieShowingByIdentifiers(imdbId: ImdbId, screenId: ScreenId): Future[Option[MovieShowing]] = {
    db.run(moviesShowing.filter(a => a.imdbId === imdbId && a.screenId === screenId).result.headOption)
  }

  def getMovieInformationByIdentifiers(imdbId: ImdbId, screenId: ScreenId): Future[Option[MovieInformation]] = {
    db.run((for {
      (movies, movieShowings) <- movies join moviesShowing
      if movies.imdbId === imdbId && movieShowings.screenId === screenId && movieShowings.imdbId === imdbId
    } yield (movies, movieShowings)).result.headOption).map(_.map {
      case (movie, movieShowing) => movieShowing.toMovieInformation(movie.movieTitle)
    })
  }

  def reserveSeat(reservation: MovieShowingReservation): Future[ServiceResult] = {
    findMovieShowingByIdentifiers(reservation.imdbId, reservation.screenId) flatMap {
      case Some(movieShowing) =>
        val seatsToReserve = reservation.seatsToReserve.getOrElse(1)
        if (movieShowing.canAccommodate(seatsToReserve)) {
          val updatedShowing = movieShowing.copy(reservedSeats = movieShowing.reservedSeats + seatsToReserve)
          db.run(moviesShowing.filter(a => a.imdbId === reservation.imdbId && a.screenId === reservation.screenId)
            .update(updatedShowing)
            .transactionally).map { _ => SeatsReservedSuccessfully }
        } else Future.successful(NoSeatsAvailable)
      case None => Future.successful(MovieShowingNotFound)
    }
  }

  override def insert(movieShowingRegistration: MovieShowingRegistration): Future[ServiceResult] = {
    def save(movie: Movie) = {
      val query = for {
        _ <- movies returning movies.map(_.id) += movie
        movieShowing = MovieShowing(
          imdbId = movieShowingRegistration.imdbId,
          screenId = movieShowingRegistration.screenId,
          availableSeats = movieShowingRegistration.availableSeats)
        moviesShowing <- moviesShowing returning moviesShowing.map(_.id) += movieShowing
      } yield moviesShowing
      db.run(query.transactionally).map(_ => MovieShowingSavedSuccessfully)
    }

    getMovieInformationByIdentifiers(movieShowingRegistration.imdbId, movieShowingRegistration.screenId) flatMap {
      case Some(_) => Future.successful(MovieShowingAlreadyExists)
      case None =>
        val movie = Movie(imdbId = movieShowingRegistration.imdbId, movieTitle = None)
        moviesService.fillMovieTitle(movie) flatMap {
          movie => movie.movieTitle match {
            case Some(_) => save(movie)
            case None =>
              logger.debug(s"Movie title could not be found for $movieShowingRegistration")
              Future.successful(MovieTitleNotFound)
          }
        } recover {
          case e: Exception =>
            logger.error(s"${e.getMessage}")
            MovieTitleNotFound
        }
    }
  }

  def getTable = moviesShowing

}

