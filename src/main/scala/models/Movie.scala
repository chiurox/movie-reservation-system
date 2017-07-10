package models

abstract class MovieIdentification(imdbId: ImdbId, screenId: ScreenId) {
  require(!imdbId.isEmpty, "imdbId.empty")
  require(!screenId.isEmpty, "screenId.empty")

  override def toString: String = s"imdbId: $imdbId - screenId: $screenId"
}

case class Movie(id: Option[MovieId] = None,
                 imdbId: ImdbId,
                 movieTitle: Option[String] = None)

case class MovieShowing(id: Option[MovieId] = None,
                        imdbId: ImdbId,
                        screenId: ScreenId,
                        availableSeats: Int,
                        reservedSeats: Int
                       ) extends MovieIdentification(imdbId, screenId) {
  require(availableSeats > 0, "availableSeats.empty")
  require(availableSeats >= reservedSeats, "reservedSeats cannot be greater than availableSeats")
}

case class MovieShowingRegistration(imdbId: ImdbId,
                                    screenId: ScreenId,
                                    availableSeats: Int
                                   ) extends MovieIdentification(imdbId, screenId)
