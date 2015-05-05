name := """bicycle-tracking"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  javaWs,
  cache,
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "jquery-ui" % "1.11.4",
  "org.webjars" % "bootstrap" % "3.3.4",
  "com.h2database" % "h2" % "1.4.187"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)


fork in run := true
