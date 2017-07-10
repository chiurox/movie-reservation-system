package services

import org.flywaydb.core.Flyway

trait DbMigrationService {

  def migrate(): Int

  def clean(): Unit

  def reload(): Int

}

class FlywayServiceImpl(jdbcUrl: String, dbUser: String, dbPass: String) extends DbMigrationService {

  private val flyway = new Flyway()
  flyway.setDataSource(jdbcUrl, dbUser, dbPass)

  override def migrate(): Int = flyway.migrate()

  override def clean(): Unit = flyway.clean()

  override def reload(): Int = {
    flyway.clean()
    flyway.migrate()
  }
}
