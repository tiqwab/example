import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val jodaTime = "joda-time" % "joda-time" % "2.9.9"
  lazy val scalikejdbc = "org.scalikejdbc" %% "scalikejdbc" % "3.0.0"
  lazy val scalikejdbcConfig = "org.scalikejdbc" %% "scalikejdbc-config" % "3.0.0"
  lazy val scalikejdbcTest = "org.scalikejdbc" %% "scalikejdbc-test" % "3.0.0"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val h2 = "com.h2database" % "h2" % "1.4.187"
}
