import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

version = "1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(compose.desktop.currentOs)
    implementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb)
        }
    }
}