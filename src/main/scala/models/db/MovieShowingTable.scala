package models.db

import models._
import services.DatabaseService


trait MovieShowingTable {

  protected val databaseService: DatabaseService
  import databaseService.driver.api._

  class MoviesShowing(tag: Tag) extends Table[MovieShowing](tag, "movie_showings") {
    def id = column[Option[MovieId]]("id", O.PrimaryKey, O.AutoInc)

    def imdbId = column[ImdbId]("imdb_id")
    def screenId = column[ScreenId]("screen_id")
    def availableSeats = column[Int]("available_seats")
    def reservedSeats = column[Int]("reserved_seats")

    def * = (id, imdbId, screenId, availableSeats, reservedSeats) <> (MovieShowing.tupled, MovieShowing.unapply)

    def pk = primaryKey("pk_a", (imdbId, screenId))

  }
  protected val moviesShowing = TableQuery[MoviesShowing]
}
