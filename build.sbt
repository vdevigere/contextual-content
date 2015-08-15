name := "sengi"

version := "1.0"

scalaVersion := "2.11.7"

organization := "org.target"

resolvers += "Local Maven Repository" at "file:///" + Path.userHome + "/.m2/repository"

libraryDependencies += "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.4"

libraryDependencies += "io.gatling.uncommons.maths" % "uncommons-maths" % "1.2.3"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.12"

libraryDependencies += "joda-time" % "joda-time" % "2.8.1"

// Lucene related dependencies
libraryDependencies += "org.apache.lucene" % "lucene-core" % "5.2.1"

libraryDependencies += "org.apache.lucene" % "lucene-queryparser" % "5.2.1"

libraryDependencies += "org.apache.lucene" % "lucene-analyzers-common" % "5.2.1"

libraryDependencies += "org.apache.lucene" % "lucene-memory" % "5.2.1"

// Undertow Container
libraryDependencies += "io.undertow" % "undertow-servlet" % "1.2.9.Final"

libraryDependencies += "io.undertow" % "undertow-core" % "1.2.9.Final"

// Scalatra - Rest Framework
libraryDependencies += "org.scalatra" % "scalatra_2.11" % "2.4.0.RC1"

libraryDependencies += "org.scalatra" %% "scalatra-json" % "2.4.0.RC1"

// Cache
libraryDependencies += "org.infinispan" % "infinispan-embedded" % "7.2.3.Final"

libraryDependencies += "com.viddu.infinispan.redis" % "infinispan-cachestore-redis" % "1.0"

// Redis - Persistent store
libraryDependencies += "redis.clients" % "jedis" % "2.7.3"

libraryDependencies += "com.google.inject" % "guice" % "4.0"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.12"

libraryDependencies += "com.typesafe.akka" % "akka-slf4j_2.11" % "2.3.12"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

// JSON Processing
libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.0-1"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.3.0.RC3"

// Test Framework and Utils
libraryDependencies += "org.scalatra" % "scalatra-scalatest_2.11" % "2.4.0.RC2-1" % Test

libraryDependencies += "org.json4s" % "json4s-jackson_2.11" % "3.3.0.RC3" % Test