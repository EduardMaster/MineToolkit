plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "net.eduard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.16-R0.2-SNAPSHOT")
    testCompile("junit", "junit", "4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}