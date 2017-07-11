package models.db

import models.{Movie, MovieId}
import services.DatabaseService

trait MovieTable {

  protected val databaseService: DatabaseService
  import databaseService.driver.api._

  class Movies(tag: Tag) extends Table[Movie](tag, "movies") {
    def id = column[Option[MovieId]]("id", O.PrimaryKey, O.AutoInc)

    def imdbId = column[String]("imdb_id")

    def movieTitle = column[Option[String]]("movie_title")

    def * = (id, imdbId, movieTitle) <> (Movie.tupled, Movie.unapply)

  }

  protected val movies = TableQuery[Movies]

}
