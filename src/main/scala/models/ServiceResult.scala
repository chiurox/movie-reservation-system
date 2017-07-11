package models

sealed trait ServiceResult {
  val message: String
}

case object MovieShowingSavedSuccessfully extends ServiceResult {
  override val message: String = "MovieShowing saved successfully"
}

case object MovieShowingAlreadyExists extends ServiceResult {
  override val message: String = "MovieShowing with imdbId + screenId already exists"
}

case object MovieTitleNotFound extends ServiceResult {
  override val message: String = "MovieTitle not found by imdbId"
}

case object MovieShowingNotFound extends ServiceResult {
  override val message: String = "MovieShowing not found by imdbId+screenId"
}

case object NoSeatsAvailable extends ServiceResult {
  override val message: String = "No seats available"
}

case object SeatsReservedSuccessfully extends ServiceResult {
  override val message: String = "Seats reserved successfully"
}
