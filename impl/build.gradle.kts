import Versions.exposed

plugins {
    kotlin("jvm")
}

version = "unspecified"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(project(":repository"))
    implementation(project(":libcnc"))
    implementation(project(":database"))
}