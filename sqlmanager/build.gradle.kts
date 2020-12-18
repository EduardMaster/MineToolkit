plugins {
    java
    kotlin("jvm")


}


group = "net.eduard.sqlmanager"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}
java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {

    compileOnly(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("stdlib-jdk8"))
    compileOnly(project(":modules"))
    testImplementation("org.bukkit:spigot:1.8.9")
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
