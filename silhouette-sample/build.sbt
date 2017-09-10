name := """silhouette-sample"""
organization := "com.tiqwab.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

val versions = new {
  val silhouette = "4.0.0"
  val macwire = "2.2.3"
}

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += cache
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
libraryDependencies += "com.mohiva" %% "play-silhouette" % versions.silhouette
libraryDependencies += "com.mohiva" %% "play-silhouette-password-bcrypt" % versions.silhouette
libraryDependencies += "com.mohiva" %% "play-silhouette-crypto-jca" % versions.silhouette
libraryDependencies += "com.mohiva" %% "play-silhouette-persistence" % versions.silhouette
libraryDependencies += "com.mohiva" %% "play-silhouette-testkit" % versions.silhouette % "test"
libraryDependencies += "org.scalikejdbc" %% "scalikejdbc" % "3.0.0"
libraryDependencies += "org.scalikejdbc" %% "scalikejdbc-config" % "3.0.0"
libraryDependencies += "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "3.0.0"
libraryDependencies += "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.5.+"
libraryDependencies += "org.skinny-framework" %% "skinny-orm" % "2.3.7"
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.6"
libraryDependencies += "com.softwaremill.macwire" %% "macros" % versions.macwire % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "util" % versions.macwire

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.tiqwab.example.controllers._"

// Adds additional packages into conf/routes
play.sbt.routes.RoutesKeys.routesImport += "models.authentication.RegistrationTokenId"

routesGenerator := InjectedRoutesGenerator

play.sbt.routes.RoutesKeys.routesImport ++= Seq(
  "controllers.CustomQueryStringBindable._",
  "controllers.CustomPathBindable._"
)

resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"
