package models

abstract class MovieIdent(imdbId: ImdbId, screenId: ScreenId) {
  require(!imdbId.isEmpty, "imdbId.empty")
  require(!screenId.isEmpty, "screenId.empty")
}

case class Movie(id: Option[MovieId] = None,
                 imdbId: ImdbId,
                 movieTitle: Option[String] = None)

//Aggregator
case class MovieInformation(imdbId: ImdbId,
                            screenId: ScreenId,
                            movieTitle: Option[String] = None,
                            availableSeats: Int,
                            reservedSeats: Int
                           ) extends MovieIdent(imdbId, screenId)

case class MovieShowing(id: Option[MovieId] = None,
                        imdbId: ImdbId,
                        screenId: ScreenId,
                        availableSeats: Int,
                        reservedSeats: Int = 0
                       ) extends MovieIdent(imdbId, screenId) {
  require(availableSeats > 0, "availableSeats.empty")
  require(availableSeats >= reservedSeats, "reservedSeats cannot be greater than availableSeats")

  def toMovieInformation(movieTitleOpt: Option[String]): MovieInformation = {
    MovieInformation(
      imdbId = this.imdbId,
      screenId = this.screenId,
      movieTitle = movieTitleOpt,
      availableSeats = this.availableSeats,
      reservedSeats = this.reservedSeats)
  }

  def canAccommodate(seatsToReserve: Int): Boolean = reservedSeats + seatsToReserve <= availableSeats
}

case class MovieShowingRegistration(imdbId: ImdbId,
                                    screenId: ScreenId,
                                    availableSeats: Int
                                   ) extends MovieIdent(imdbId, screenId) {
  require(availableSeats > 0, "availableSeats.empty")
}

case class MovieShowingReservation(imdbId: ImdbId,
                                   screenId: ScreenId,
                                   seatsToReserve: Option[Int] = None
                                  ) extends MovieIdent(imdbId, screenId)

