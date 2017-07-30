import Dependencies._

// scalacOptions
// See `scalac -help`, `scalac -X`, or `scalac -Y`
lazy val commonScalacOptions = Seq(
  "-feature" // Emit warning and location for usages of features that should be imported explicitly.
  , "-deprecation" // Emit warning and location for usages of deprecated APIs.
  , "-unchecked" // Enable additional warnings where generated code depends on assumptions.
  , "-encoding" // Specify encoding of source files
  , "UTF-8"
  // , "-Xfatal-warnings"
  , "-language:_"
  , "-Ywarn-adapted-args" // Warn if an argument list is modified to match the receiver
  , "-Ywarn-dead-code" // Warn when dead code is identified.
  , "-Ywarn-inaccessible" // Warn about inaccessible types in method signatures.
  , "-Ywarn-infer-any" // Warn when a type argument is inferred to be `Any`.
  , "-Ywarn-nullary-override" // Warn when non-nullary `def f()' overrides nullary `def f'
  , "-Ywarn-nullary-unit" // Warn when nullary methods return Unit.
  , "-Ywarn-numeric-widen" // Warn when numerics are widened.
  , "-Ywarn-unused" // Warn when local and private vals, vars, defs, and types are unused.
  , "-Ywarn-unused-import" // Warn when imports are unused.
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.tiqwab.example",
      scalaVersion := "2.12.2",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "redis-pubsub",
    scalacOptions := commonScalacOptions,
    libraryDependencies ++= Seq(
      scalaTest % Test,
      akkaSlf4j,
      akkaStream,
      akkaActor,
      logbackClassic,
      logbackCore,
      jedisClient
    )
  )

// To execute scalafmt from sbt 0.13
def latestScalafmt = "1.0.0-RC4"
commands += Command.args("scalafmt", "Run scalafmt cli.") {
  case (state, args) =>
    val Right(scalafmt) =
      org.scalafmt.bootstrap.ScalafmtBootstrap.fromVersion(latestScalafmt)
    scalafmt.main("--non-interactive" +: args.toArray)
    state
}
