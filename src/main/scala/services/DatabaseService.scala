package services

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

trait DatabaseService {
  val driver: slick.jdbc.H2Profile
  val db: driver.backend.DatabaseDef
}

class DatabaseServiceImpl(jdbcUrl: String, dbUser: String, dbPass: String) extends DatabaseService {
  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(jdbcUrl)
  hikariConfig.setUsername(dbUser)
  hikariConfig.setPassword(dbPass)

  private val dataSource = new HikariDataSource(hikariConfig)
  override val driver = slick.jdbc.H2Profile
  import driver.api._

  override val db = Database.forDataSource(dataSource, None)
  db.createSession()

}
