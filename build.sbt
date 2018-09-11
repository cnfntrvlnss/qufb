name := """play-java-qufb"""

version := "1.0-SNAPSHOT"

javacOptions ++= Seq("-encoding", "UTF-8")

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.6")
PlayKeys.externalizeResources := false
PlayKeys.devSettings += "play.server.http.port" -> "8080"


libraryDependencies += guice
libraryDependencies += javaJpa
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.17.Final"
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "be.objectify" %% "deadbolt-java" % "2.6.3"
libraryDependencies += javaWs % "test"

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % "test"
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % "test"
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.2"