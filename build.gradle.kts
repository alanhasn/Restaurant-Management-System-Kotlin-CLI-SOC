plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "com.restaurant"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

}


application {
    mainClass.set("MainKt")
}
