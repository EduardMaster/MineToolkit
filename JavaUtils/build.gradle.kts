plugins {
    java
    kotlin("jvm")
    `maven-publish`
}

group = "net.eduard.lib"
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
            groupId = "net.eduard"
            artifactId = "javautils"
            version = project.version as String
            from(components["java"])
        }
    }
}


repositories {
    mavenLocal()

}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("org.bukkit:spigot:1.8.9")
}
