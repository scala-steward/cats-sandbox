name := "cats-sandbox"
version := "0.0.1-SNAPSHOT"

scalafixDependencies in ThisBuild +=
  "com.nequissimus" %% "sort-imports" % "0.3.2"

val format = taskKey[Unit]("Format files using scalafmt and scalafix")

val CatsEffectVersion = "2.1.3"
val CatsVersion = "2.1.1"
val LogbackVersion = "1.2.3"
val MunitVersion = "0.7.3"

lazy val root = (project in file("."))
  .settings(
    organization := "asachdeva",
    name := "cats-sandbox",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.scalameta" %% "munit" % MunitVersion % Test,
      "org.typelevel" %% "cats-core" % CatsVersion,
      "org.typelevel" %% "cats-effect" % CatsEffectVersion
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin(scalafixSemanticdb),
    testFrameworks := List(new TestFramework("munit.Framework")),
    format := {
      Command.process("scalafmtAll", state.value)
      Command.process("scalafmtSbt", state.value)
      Command.process("scalafix", state.value)
      Command.process("scalafix RemoveUnused", state.value)
      Command.process("test:scalafix", state.value)
      Command.process("test:scalafix RemoveUnused", state.value)
    }
  )

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint", // enable handy linter warnings
  "-Xfatal-warnings", // turn compiler warnings into errors
  "-Ywarn-unused"
)

resolvers += Resolver.sonatypeRepo("releases")

// CI build
addCommandAlias("buildCatsSandbox", ";clean;+test;")
