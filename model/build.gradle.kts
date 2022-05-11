import Versions.coroutines
import Versions.exposed
import Versions.kodein
import Versions.sqliteJdbc

plugins {
    kotlin("jvm")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
}