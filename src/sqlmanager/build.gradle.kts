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
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("net.eduard:modules:1.0-SNAPSHOT")
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