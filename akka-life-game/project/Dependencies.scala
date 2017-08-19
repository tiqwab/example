import sbt._

object Dependencies {
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.4.18"
  lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % "2.4.18"
  lazy val akkaTestKit = "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.18"
  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val logbackCore = "ch.qos.logback" % "logback-core" % "1.2.3"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
}
