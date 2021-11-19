import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.21"
}

group = "com.gavinflood.fpl.api"
version = "1.0-SNAPSHOT"

var junitVersion = "5.7.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.4")
    implementation("com.squareup.okhttp3:mockwebserver:4.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}