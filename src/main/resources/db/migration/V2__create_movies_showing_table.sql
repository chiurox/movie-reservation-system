CREATE TABLE IF NOT EXISTS "movies_showing" (
  "id"              BIGSERIAL PRIMARY KEY AUTO_INCREMENT,
  "imdb_id"        VARCHAR NOT NULL,
  "screen_id"      VARCHAR NOT NULL,
  "available_seats"  VARCHAR NOT NULL,
  "reserved_seats"  VARCHAR NOT NULL,
);

ALTER TABLE "movies_showing" ADD CONSTRAINT "MOVIE_FK" FOREIGN KEY ("imdb_id") REFERENCES "movies" ("imdb_id") ON UPDATE RESTRICT ON DELETE CASCADE;

