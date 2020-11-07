plugins {
    java
    kotlin("jvm") version "1.3.72"
    `maven-publish`
}

group = "net.eduard"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.bukkit:spigot:1.8.9")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.16-R0.2-SNAPSHOT")
    compileOnly(kotlin("stdlib"))
    testImplementation("junit", "junit", "4.12")
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.eduard"
            artifactId = "modules"
            version = project.version as String

            from(components["java"])
        }
    }
}

tasks {
    compileJava{
       options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"

    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}