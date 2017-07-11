package services

import models._
import models.db.MovieTable
import utils.ActorContext

import scala.concurrent.Future
import scala.language.postfixOps

trait MoviesService {

  def getMovies: Future[Seq[Movie]]

  def findById(id: MovieId): Future[Option[Movie]]

  def findByImdbId(imdbId: ImdbId): Future[Option[Movie]]

  def insert(movie: Movie): Future[Movie]

  def fillMovieTitle(movie: Movie): Future[Movie]

  def update(movie: Movie): Future[Option[Movie]]

  def delete(imdbId: ImdbId): Future[Int]

}

class MoviesServiceImpl(val databaseService: DatabaseService,
                        val movieTitlesService: MovieTitlesService)
  extends MovieTable with MoviesService with ActorContext {

  import databaseService._
  import databaseService.driver.api._

  override def getMovies: Future[Seq[Movie]] = db.run(movies.result)

  override def findById(id: MovieId): Future[Option[Movie]] = db.run(movies.filter(_.id === id).result.headOption)

  override def findByImdbId(imdbId: ImdbId): Future[Option[Movie]] = db.run(movies.filter(_.imdbId === imdbId).result.headOption)

  override def insert(movie: Movie): Future[Movie] = {
    db.run(movies returning movies.map(_.id) += movie).map(movieId => movie.copy(id = movieId))
  }

  override def fillMovieTitle(movie: Movie): Future[Movie] = {
    movieTitlesService.fetchTitle(movie.imdbId) map {
      case Some(movieTitle) => movie.copy(movieTitle = Some(movieTitle))
      case None => movie
    }
  }

  override def update(movie: Movie): Future[Option[Movie]] = {
    db.run(movies.filter(m => m.imdbId === movie.imdbId).update(movie).transactionally).map(_ => Some(movie))
  }

  override def delete(imdbId: ImdbId): Future[Int] = {
    db.run(movies.filter(movie => movie.imdbId === imdbId).delete.transactionally)
  }

  def getTable = movies

}

