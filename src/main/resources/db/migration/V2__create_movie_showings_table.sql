CREATE TABLE IF NOT EXISTS "movie_showings" (
  "id"              BIGSERIAL AUTO_INCREMENT,
  "imdb_id"         VARCHAR NOT NULL,
  "screen_id"       VARCHAR NOT NULL,
  "available_seats" VARCHAR NOT NULL,
  "reserved_seats"  VARCHAR NOT NULL,
  PRIMARY KEY(imdb_id, screen_id)
);

ALTER TABLE "movie_showings" ADD CONSTRAINT "MOVIE_FK" FOREIGN KEY ("imdb_id") REFERENCES "movies" ("imdb_id") ON UPDATE RESTRICT ON DELETE CASCADE;

