name := "Movie Reservation System"

version := "1.0"

scalaVersion := "2.12.2"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {

  val akkaHttpVersion = "10.0.9"
  val akkaVersion = "2.4.19"
  val scalaTestVersion = "3.0.3"
  val scalaMockVersion = "3.6.0"
  val slickVersion = "3.2.0"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.1",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.h2database" % "h2" % "1.4.196",
    "org.flywaydb" % "flyway-core" % "4.2.0",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.specs2" %% "specs2-core" % "3.8.9" % "test",
    "org.specs2" %% "specs2-mock" % "3.8.9"
  )
}
parallelExecution in Test := false
