import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.4"
  lazy val scalikejdbc = "org.scalikejdbc" %% "scalikejdbc" % "3.0.0"
  lazy val scalikejdbcConfig = "org.scalikejdbc" %% "scalikejdbc-config" % "3.0.0"
  lazy val scalikejdbcSyntaxSupportMacro = "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "3.0.0"
  lazy val skinnyOrm = "org.skinny-framework" %% "skinny-orm" % "2.3.7"
  lazy val mysql = "mysql" % "mysql-connector-java" % "6.0.6"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
}
