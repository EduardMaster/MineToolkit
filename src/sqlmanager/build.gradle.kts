plugins {
    java
    kotlin("jvm") version "1.3.72"
    `maven-publish`

}

group = "net.eduard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {

    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("net.eduard:modules:1.0-SNAPSHOT")
    testImplementation("org.bukkit:spigot:1.8.9")
    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("junit", "junit", "4.12")

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.eduard"
            artifactId = "sqlmanager"
            version = project.version as String

            from(components["java"])
        }
    }
}