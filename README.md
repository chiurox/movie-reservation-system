Movie Reservation System
=========

Simple movie showing reservation system

* Requirements described on this [doc](https://github.com/chiurox/movie-reservation-system/blob/master/Specification.md)
* Akka-http slick base skeleton taken from this [repo](https://github.com/BBartosz/akkaRestApi)

### Base Stack
 - [Scala](https://www.scala-lang.org/)
 - [Akka Http](https://github.com/akka/akka-http)
 - [Spray Json](https://github.com/spray/spray-json)
### Database and relational ORM
 - [H2](https://www.h2database.com/) (for in memory database mimicking Postgres)
 - [Flyway](https://github.com/flyway/flyway) (for database migrations)
 - [Slick](https://github.com/slick/slick)
### Testing Frameworks:
 - [ScalaTest](http://www.scalatest.org/)
 - [Specs2](https://github.com/etorreborre/specs2)
 - [Mockito](https://github.com/mockito/mockito)

### API documentation can be found on APIARY [here](http://docs.moviereservationsystem.apiary.io)

### Functionality
 - Movie & MovieShowing CRUD
 - Movie Title fetching from third party API (http://api.myapifilms.com)
 - Database schema migration
 - API route tests

### Considerations
 - Using two entities Movies and MovieShowings in order to separate responsibility
 - Movie hold only information pertaining to movie data, such as title. Makes it easier to be extended to hold more data such as description, director, etc
 - MovieShowing have a relation to Movie, but only holds information about the screening session, such as seat availability
 - Movie title is not given upon movie creation, so a call to an external API has to be done, which also validates the imdbId and keeps the database leaner


### Test
```sh
sbt test
```
### Test output:
```aidl
[info] MovieShowingsRouteSpec:
[info] Movie Showings Routes
[info] - should return list of all movies showing
[info] - should return movie informations by querydtring params imdbId and screenId
[info] - should return not found for inexistent imdbId and screenId
[info] - should register a movie showing
[info] - should reserve a seat at a movie showing
[info] - should not reserve a seat if movie showing is beyond capacity
[info] - should not reserve a seat if movie showing can't be found by imdbId + screenId
[info] MoviesRouteSpec:
[info] Movies Routes
[info] - should return list of all movies
[info] - should return list of movies by querystring imdbId
[info] - should create a movie
[info] - should update a movie
[info] - should delete a movie
[info] ScalaTest
[info] Run completed in 3 seconds, 143 milliseconds.
[info] Total number of tests run: 12
[info] Suites: completed 2, aborted 0
[info] Tests: succeeded 12, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```

### Run
```sh
sbt run
```

## Improvements
 - Use Circe or Argonaut instead of spray-json
 - Use Postgres for production
 - Caching mechanism with Redis to offload requests to database
 - Locking mechanism that gives user X amount of time to complete the movie showing reservation (can be done via Redis ttl)
 - Docker container for database, redis
 - API documentation with Swagger instead of apiary
 - Deploy to a cloud provider such as Amazon or Heroku
 - Pagination with Slick
 - CORS support
 - User accounts
 - OAuth2
 - Allow X number of reservations coming from a user (prevents ticket scalping)
