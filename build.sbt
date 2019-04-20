val root = (project in file("."))
  .settings(
    name := "wallet-scala",
    version := "0.1",
    scalaVersion := "2.12.8",
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
      "-encoding",
      "UTF-8",
      "-Xfatal-warnings",
      "-language:_",
      // Warn if an argument list is modified to match the receiver
      "-Ywarn-adapted-args",
      // Warn when dead code is identified.
      "-Ywarn-dead-code",
      // Warn about inaccessible types in method signatures.
      "-Ywarn-inaccessible",
      // Warn when a type argument is inferred to be `Any`.
      "-Ywarn-infer-any",
      // Warn when non-nullary `def f()' overrides nullary `def f'
      "-Ywarn-nullary-override",
      // Warn when nullary methods return Unit.
      "-Ywarn-nullary-unit",
      // Warn when numerics are widened.
      "-Ywarn-numeric-widen",
      // Warn when imports are unused.
      "-Ywarn-unused-import"
    ),
    scalafmtOnCompile in ThisBuild := true,
    libraryDependencies ++= Seq(
      "org.sisioh" %% "baseunits-scala" % "0.1.21",
      "com.beachape" %% "enumeratum" % "1.5.13",
      "io.monix" %% "monix" % "3.0.0-RC2",
      "io.azam.ulidj" % "ulidj" % "1.0.0"
    )
  )