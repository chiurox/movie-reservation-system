package services

import models.db.{MovieShowingTable, MovieTable}
import models._
import slick.sql.{FixedSqlStreamingAction, SqlAction}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import com.typesafe.scalalogging.StrictLogging

trait MoviesService {

  def getMovies: Future[Seq[Movie]]

  def findById(id: MovieId): Future[Option[Movie]]

  def findByImdbId(imdbId: ImdbId): Future[Seq[Movie]]

  def insert(movie: Movie): Future[Movie]

  def update(movie: Movie): Future[Movie]

  def delete(imdbId: ImdbId): Future[ImdbId]
}

class MoviesServiceImpl(val databaseService: DatabaseService)(implicit executionContext: ExecutionContext)
  extends MovieTable with MoviesService with StrictLogging {

  import databaseService._
  import databaseService.driver.api._

  override def getMovies: Future[Seq[Movie]] = movies.result

  override def findById(id: MovieId): Future[Option[Movie]] = movies.filter(_.id === id).result.headOption

  override def findByImdbId(imdbId: ImdbId): Future[Seq[Movie]] = movies.filter(_.imdbId === imdbId).result

  override def insert(movie: Movie): Future[Movie] = movies returning movies += movie

  override def update(movie: Movie): Future[Movie] = {
    db.run(movies.filter(m => m.imdbId === movie.imdbId).update(movie).transactionally.map(_ => movie))
  }

  override def delete(imdbId: ImdbId): Future[ImdbId] = {
    db.run(movies.filter(movie => movie.imdbId === imdbId).delete.transactionally).map(_ => imdbId)
  }

  //TODO: extract implicit helper methods to another util
  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }

  def getTable = movies

}

