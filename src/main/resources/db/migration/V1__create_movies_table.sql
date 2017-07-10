CREATE TABLE IF NOT EXISTS "movies" (
  "id"              BIGSERIAL AUTO_INCREMENT PRIMARY KEY,
  "imdb_id"         VARCHAR NOT NULL,
  "movie_title"     VARCHAR
);

