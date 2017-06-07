import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val jodaTime = "joda-time" % "joda-time" % "2.9.9"
  lazy val scalikejdbc = "org.scalikejdbc" %% "scalikejdbc" % "2.5.1"
  lazy val scalikejdbcConfig = "org.scalikejdbc" %% "scalikejdbc-config" % "2.5.1"
}
