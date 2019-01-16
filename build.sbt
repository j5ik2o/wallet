name := "wallet-scala"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "org.sisioh" %% "baseunits-scala" % "0.1.21",
  "com.beachape" %% "enumeratum" % "1.5.13",
  "io.monix" %% "monix" % "3.0.0-RC2",
  "io.azam.ulidj" % "ulidj" % "1.0.0"
)