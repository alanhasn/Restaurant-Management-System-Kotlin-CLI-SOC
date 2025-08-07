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
    implementation("at.favre.lib:bcrypt:0.9.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("io.mockk:mockk:1.13.8")



}

tasks.test {
    useJUnitPlatform()
}


application {
    mainClass.set("MainKt")
}
