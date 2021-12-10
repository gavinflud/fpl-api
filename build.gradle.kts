import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.21"
    id("org.jetbrains.dokka") version "1.6.0"
    `maven-publish`
    id("signing")
}

group = "com.gavinflood.fpl.api"
version = "1.0.1"

var junitVersion = "5.8.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")
    implementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    implementation("org.ojalgo:ojalgo:49.2.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("javadoc"))
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].java.srcDirs)
}

val javaDocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from("$buildDir/javadoc")
}

publishing {
    publications {
        create<MavenPublication>("fpl-api") {
            groupId = group.toString()
            artifactId = "fpl-api"
            version = version

            signing {
                sign(publishing.publications["fpl-api"])
            }

            from(components["java"])

            artifact(sourcesJar)
            artifact(javaDocJar)

            pom {
                name.set("Fantasy Premier League API Wrapper")
                description.set("A Kotlin wrapper around the Fantasy Premier League API.")
                url.set("https://github.com/gavinflud/fpl-api")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("gavinflud")
                        name.set("Gavin Flood")
                        email.set("gavin@gavinflood.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/gavinflud/fpl-api.git")
                    developerConnection.set("scm:git:ssh://github.com:gavinflud/fpl-api.git")
                    url.set("https://github.com/gavinflud/fpl-api")
                }
                distributionManagement {
                    repositories {
                        maven {
                            credentials {
                                username = project.properties["mavenUsername"] as String?
                                password = project.properties["mavenPassword"] as String?
                            }

                            if (version.endsWith("-SNAPSHOT")) {
                                setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots")
                            } else {
                                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
                            }
                        }
                    }
                }
            }
        }
    }
}