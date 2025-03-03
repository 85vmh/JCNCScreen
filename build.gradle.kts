buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:${Versions.compose}")
    }
}

plugins {
    kotlin("jvm") version Versions.kotlin apply false
    //id("org.jetbrains.compose") version "1.0.0-alpha3" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

//tasks.create<Exec>("cleanNatives") {
//    val path = projectDir.path + "/native"
//    //commandLine("make clean -C $path")
//    commandLine("echo", "ceva bun")
//}
//
//tasks.create<Exec>("buildNatives") {
//    val path = projectDir.path + "/native"
//    commandLine("make -C $path")
//}