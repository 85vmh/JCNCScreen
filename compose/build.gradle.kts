import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

version = "1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation(compose.desktop.currentOs)
    implementation(compose.uiTooling)
    @OptIn(ExperimentalComposeLibrary::class)
    implementation(compose.material3)
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation(project(":libcnc"))
    implementation(project(":repository"))
    implementation("org.kodein.di:kodein-di-framework-compose:${Versions.kodein}")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs("-Djava.library.path=${NativePaths.getNativePaths(rootProject).joinToString(":")}")
        nativeDistributions {
            targetFormats(TargetFormat.Deb)
        }
    }
}

tasks.register("distcnc") {
    dependsOn(tasks.getByName("createDistributable"))
    val file = File(projectDir, "KtCnc")
    file.setWritable(true)
    file.setExecutable(true)
    file.writeText(
        """#!/bin/bash
java -Djava.library.path=${NativePaths.getNativePaths(rootProject).joinToString(":")} -jar compose-all.jar -base ./lib
"""
    )
}