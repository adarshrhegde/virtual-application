name := "css441_hw4"

version := "0.1"

scalaVersion := "2.12.8"

lazy val slf4jVersion = "1.7.5"
lazy val logbackVersion = "1.2.3"
lazy val pureConfigVersion = "0.10.0"
lazy val scalaTestVersion = "3.0.5"
lazy val springVersion = "1.5.4.RELEASE"

/*
  Project dependencies
 */
libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
  "org.springframework.boot" % "spring-boot-starter-web" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-test" % springVersion,
  "org.springframework.boot" % "spring-boot-configuration-processor" % springVersion,
  "io.rest-assured" % "rest-assured" % "3.0.0" % "test"
)

/*
  Packaging plugin
 */

// enable the Java app packaging archetype and Ash script (for Alpine Linux, doesn't have Bash)
enablePlugins(JavaAppPackaging, AshScriptPlugin)

// set the main entrypoint to the application that is used in startup scripts
mainClass in (Compile) := Some("com.uic.chessvap.ChessVapApplication")

packageName in Docker := "chessgame"

version in Docker := "1.1.0"

// the Docker image to base on (alpine is smaller than the debian based one (120 vs 650 MB)
dockerBaseImage := "anapsix/alpine-java"

dockerRepository := Some("adarsh23")

// creates tag 'latest' as well when publishing
dockerUpdateLatest := true


