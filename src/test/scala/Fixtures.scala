package services
import models.Movie

import scala.language.postfixOps

object Fixtures {

  val testMovies = Seq(
    Movie(id = Some(1), imdbId = "tt0111161", movieTitle = Some("The Shawshank Redemption")),
    Movie(id = Some(2), imdbId = "tt0133093", movieTitle = Some("The Matrix"))
  )

}
