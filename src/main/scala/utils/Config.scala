package utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val databaseConfig = config.getConfig("database")
  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val dbUrl = databaseConfig.getString("url")
  val dbUser = databaseConfig.getString("user")
  val dbPass = databaseConfig.getString("password")

}
