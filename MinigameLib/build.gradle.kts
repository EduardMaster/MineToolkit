plugins {
    java
    kotlin("jvm")
    `maven-publish`
}

group = "net.eduard.bukkitlibs"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.eduard.bukkitlibs"
            artifactId = "minigame"
            version = project.version as String
            from(components["java"])
        }
    }
}


repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")

}

dependencies {
    compileOnly(project(":JavaUtils"))
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly(kotlin("stdlib"))
}
