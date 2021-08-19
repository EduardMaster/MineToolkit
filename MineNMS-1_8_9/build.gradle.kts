plugins {
    java
    kotlin("jvm")
    `maven-publish`
}

group = "net.eduard.lib"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
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
            groupId = "net.eduard.minenms"
            artifactId = "impl_1_8_9"
            version = project.version as String
            from(components["java"])
        }
    }
}


repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(project(":MineNMS"))
    compileOnly(project(":MineUtils"))
    compileOnly(project(":JavaUtils"))
    compileOnly("org.bukkit:spigot:1.8.9")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly(kotlin("stdlib"))
}
