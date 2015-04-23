name := """bicycle-tracking"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  javaWs,
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "jquery-ui" % "1.11.4",
  "org.webjars" % "bootstrap" % "3.3.4",
  "com.h2database" % "h2" % "1.4.187",
  "org.springframework" % "spring-context" % "4.1.6.RELEASE",
  "org.springframework" % "spring-orm" % "4.1.6.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.1.6.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.6.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.6.RELEASE",
  "org.springframework" % "spring-aop" % "4.1.6.RELEASE",
  "org.springframework" % "spring-test" % "4.1.6.RELEASE" % "test",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)


fork in run := true
