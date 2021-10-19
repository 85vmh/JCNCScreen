import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

version = "1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation(compose.desktop.currentOs)
    implementation(compose.uiTooling)
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation(project(":libcnc"))
    implementation(project(":repository"))
    implementation(project(":dummycnc"))
    implementation("org.kodein.di:kodein-di-framework-compose:7.8.0")
}

val nativePath = File(rootProject.projectDir,"libcnc/native").path

compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs("-Djava.library.path=$nativePath:$nativePath/lc/lib")
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
    file.writeText("""#!/bin/bash
java -Djava.library.path=$nativePath:$nativePath/lc/lib -jar compose-all.jar -base ./lib
""")
}