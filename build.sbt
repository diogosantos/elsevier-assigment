name := "elsevier-assingment"
version := "0.1"
scalaVersion := "2.12.8"

enablePlugins(JavaAppPackaging)

mainClass in assembly := Some("Main")
assemblyJarName in assembly := "elsevier.jar"

libraryDependencies += "com.47deg" %% "github4s" % "0.20.1"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.27"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.25.0" % Test