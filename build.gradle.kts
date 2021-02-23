plugins {
    java
    kotlin("jvm") version "1.4.21"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}
group = "net.eduard"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
dependencies {
    compileOnly(kotlin("stdlib"))
    api(project(":MineNMS"))
    api(project(":MineNMS-1_8_9"))
    api(project(":MineNMS-1_7"))
    api(project(":MineNMS-1_12"))
    api(project(":JavaUtils"))
    api(project(":MineUtils"))
    api(project(":SQLManager"))
    compileOnly("org.bukkit:spigot:1.8.9")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.16-R0.2-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("net.bukkitplugin:jhcash:6.1")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
}
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>{
    destinationDir = file("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\plugins\\")
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}