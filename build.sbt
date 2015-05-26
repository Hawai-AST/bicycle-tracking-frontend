import com.github.play2war.plugin._

name := """bicycle-tracking"""

version := "1.0-SNAPSHOT"

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.1"

libraryDependencies ++= Seq(
  javaCore,
  javaWs,
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "jquery-ui" % "1.11.4",
  "org.webjars" % "bootstrap" % "3.3.4"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)


//fork in run := true
