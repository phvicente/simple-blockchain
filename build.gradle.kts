val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "1.8.0"
    id("io.ktor.plugin") version "3.0.0-rc-1"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")

    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")

    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-content-negotiation")

    implementation("org.rocksdb:rocksdbjni:8.0.0")

    implementation("io.insert-koin:koin-ktor:4.0.0")
    implementation("io.insert-koin:koin-logger-slf4j:4.0.0")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
