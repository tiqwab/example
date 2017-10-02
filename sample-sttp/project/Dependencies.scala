import sbt._

object Dependencies {
  lazy val sttpCore = "com.softwaremill.sttp" %% "core" % "0.0.16"
  lazy val sttpFuture = "com.softwaremill.sttp" %% "async-http-client-backend-future" % "0.0.16"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
}
