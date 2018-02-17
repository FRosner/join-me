lazy val root = (project in file("."))
  .settings(
    name := "join-me",
    startYear := Some(2018),
    organization := "frosner",
    description := "Implementation of different join algorithms for fun.",
    homepage := Some(url(s"https://github.com/FRosner/join-me")),
    licenses += "Apache 2" -> url("https://www.apache.org/licenses/LICENSE-2.0"),
    scalaVersion := "2.12.1",
    // A set of useful dependencies
    libraryDependencies ++= List(
      // Test framework
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    ),
    scalacOptions ++= List(
      // Code encoding
      "-encoding",
      "UTF-8",
      // Deprecation warnings
      "-deprecation",
      // Warnings about features that should be imported explicitly
      "-feature",
      // Enable additional warnings about assumptions in the generated code
      "-unchecked",
      // Recommended additional warnings
      "-Xlint",
      // Warn when argument list is modified to match receiver
      "-Ywarn-adapted-args",
      // Warn about dead code
      "-Ywarn-dead-code",
      // Warn about inaccessible types in signatures
      "-Ywarn-inaccessible",
      // Warn when non-nullary overrides a nullary (def foo() over def foo)
      "-Ywarn-nullary-override",
      // Warn when numerics are unintentionally widened
      "-Ywarn-numeric-widen",
      // Fail compilation on warnings
      "-Xfatal-warnings"
    )
  )
