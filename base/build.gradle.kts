plugins {
    kotlin("jvm")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":libcnc"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.kodein.di:kodein-di-framework-compose:7.8.0")
}