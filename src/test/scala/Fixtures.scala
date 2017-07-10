package services
import models.{Movie, MovieInformation, MovieShowing}

import scala.language.postfixOps

object Fixtures {

  val testMovies = Seq(
    Movie(id = Some(1), imdbId = "tt0111161", movieTitle = Some("The Shawshank Redemption")),
    Movie(id = Some(2), imdbId = "tt0133093", movieTitle = Some("The Matrix")),
    Movie(id = Some(3), imdbId = "tt0816692", movieTitle = Some("Interstellar"))
  )
  val testMovieShowings = testMovies.zipWithIndex.map {
    case (movie, index) => MovieShowing(id = movie.id, imdbId = movie.imdbId, screenId = s"screen_$index", availableSeats = 10, reservedSeats = 0)
  }
  val testMovieInformations = testMovies.zip(testMovieShowings) map { a =>
    val movie = a._1
    val showing = a._2
    MovieInformation(
      imdbId = movie.imdbId,
      screenId = showing.screenId,
      availableSeats = showing.availableSeats,
      reservedSeats = showing.reservedSeats,
      movieTitle = movie.movieTitle)
  }

}
