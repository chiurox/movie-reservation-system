CREATE TABLE IF NOT EXISTS "movies" (
  "id"              BIGSERIAL AUTO_INCREMENT PRIMARY KEY,
  "imdb_id"         VARCHAR NOT NULL UNIQUE,
  "movie_title"     VARCHAR
);

--INSERT INTO movies (imdb_id, movie_title) values ('t111', 'cloverfield');
--INSERT INTO movies (imdb_id, movie_title) values ('t112', 'matrix');
--INSERT INTO movies (imdb_id, movie_title) values ('t113', 'lotr');

