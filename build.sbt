import sbt.Keys._
import sbt._
import sbtrelease.Version

name := "hello"

resolvers += Resolver.sonatypeRepo("public")
scalaVersion := "2.12.6"
releaseNextVersion := { ver =>
  Version(ver).map(_.bumpMinor.string).getOrElse("Error")
}
assemblyJarName in assembly := "hello.jar"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "2.2.1",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.eed3si9n" %% "gigahorse-okhttp" % "0.3.0",
  "io.circe" %% "circe-core" % "0.9.3",
  "io.circe" %% "circe-generic" % "0.9.3",
  "io.circe" %% "circe-parser" % "0.9.3",
  "com.typesafe" % "config" % "1.3.2",
  "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.386",
  "com.amazonaws" % "aws-java-sdk-core" % "1.11.386",
  "com.amazonaws" % "jmespath-java" % "1.11.386",
)

scalacOptions ++= Seq("-unchecked",
                      "-deprecation",
                      "-feature",
                      "-Xfatal-warnings")
