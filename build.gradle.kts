plugins {
    kotlin("jvm") version "2.2.0"
    application

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Kotlin Standard Library
    implementation(kotlin("stdlib"))

    // Kotlin Coroutines Core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // For colored output or CLI formatting
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta5")

    // For hashing passwords
    implementation("at.favre.lib:bcrypt:0.9.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}
