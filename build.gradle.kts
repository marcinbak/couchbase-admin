import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.2.0"
val springBootVersion = "2.0.0.M7"
val jUnitVersion = "5.0.0"
val bootstrapVersion = "3.3.7"

buildscript {

  val kotlinVersion = "1.2.0"
  val springBootVersion = "2.0.0.M7"

  repositories {
    mavenCentral()
    maven("https://repo.spring.io/snapshot")
    maven("https://repo.spring.io/milestone")
  }
  dependencies {
    classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
  }
}

plugins {
  kotlin("jvm").version("1.2.0")

}

apply {
  plugin("kotlin")
  plugin("kotlin-spring")
  plugin("eclipse")
  plugin("org.springframework.boot")
  plugin("io.spring.dependency-management")
}

group = "pl.neofonie.bak"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }
}

repositories {
  mavenCentral()
  maven("https://repo.spring.io/snapshot")
  maven("https://repo.spring.io/milestone")
}

dependencies {
//  compile("org.springframework.boot:spring-boot-starter-data-couchbase-reactive")
  compile("org.springframework.boot:spring-boot-starter-thymeleaf")
  compile("org.springframework.boot:spring-boot-starter-web")
  compile("org.webjars:bootstrap:$bootstrapVersion")
  compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
  compile("org.jetbrains.kotlin:kotlin-reflect")
  runtime("org.springframework.boot:spring-boot-devtools")
  testCompile("org.springframework.boot:spring-boot-starter-test")
}
