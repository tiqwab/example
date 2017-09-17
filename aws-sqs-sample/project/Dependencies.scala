import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val awsSqs = "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.196"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
}
