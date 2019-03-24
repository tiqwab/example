// scalacOptions
// See `scalac -help`, `scalac -X`, or `scalac -Y`
lazy val commonScalacOptions = Seq(
  "-feature" // Emit warning and location for usages of features that should be imported explicitly.
  , "-deprecation" // Emit warning and location for usages of deprecated APIs.
  , "-unchecked" // Enable additional warnings where generated code depends on assumptions.
  , "-Xlint"
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

lazy val commonSettings = Seq(
  organization := "com.example",
  scalaVersion := "2.12.8",
  version      := "0.1.0-SNAPSHOT",
  scalacOptions := commonScalacOptions,
  scalacOptions in (Compile, console) -= "-Ywarn-unused",
  scalacOptions in (Compile, console) -= "-Ywarn-unused-import",
  scalacOptions in (Compile, console) -= "-Xlint",
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
)

lazy val versions = new {
    val logback = "1.2.3"
    val scalaLogging = "3.9.0"
    val scalaTest = "3.0.5"
}

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "get-along-with-implicit",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "com.typesafe.scala-logging" %% "scala-logging" % versions.scalaLogging,
      "org.scalatest" %% "scalatest" % versions.scalaTest % Test
    )
  )
