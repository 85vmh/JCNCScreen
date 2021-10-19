import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

version = "unspecified"

repositories {
    mavenCentral()
}
val nativePath = File(rootProject.projectDir,"libcnc/native").path

application {
    mainClassName = "de.schwarzrot.app.LinuxCNCClient"
    applicationDefaultJvmArgs = listOf("-Djava.library.path=$nativePath:$nativePath/lc/lib")
}

tasks.withType<Jar> {
    manifest {
        attributes(mapOf("Main-Class" to "de.schwarzrot.app.LinuxCNCClient"))
    }
}

tasks.withType<ShadowJar> {
    //destinationDir(projectDir)
}

tasks.register("distcnc") {
    dependsOn(tasks.getByName("shadowJar"))
    val file = File(projectDir, "KtCnc")
    file.setWritable(true)
    file.setExecutable(true)
    file.writeText("""#!/bin/bash
java -Djava.library.path=$nativePath:$nativePath/lc/lib -jar app-all.jar -base ./lib
""")
}

dependencies {
    implementation(project(":libcnc"))
    implementation(project(":repository"))
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    //implementation fileTree(dir: 'lib', include: ['*.jar'])
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

//model {
//    platforms {
//        x64 {
//            architecture "x64"
//        }
//    }
//
//    components {
//        cnc(NativeLibrarySpec) {
//            binaries.all {
//                cCompiler.args '-I', "${org.gradle.internal.jvm.Jvm.current().javaHome}/include"
//                cCompiler.args '-I', "${org.gradle.internal.jvm.Jvm.current().javaHome}/include/linux"
//                def linuxcncPath = "/home/cnc/Work/linuxcnc-dev"
//                cCompiler.args '-I', "${linuxcncPath}/native/lc/src"
//                cCompiler.args '-I', "${linuxcncPath}/native/lc/include"
//                cCompiler.args '-L', "${linuxcncPath}/native/lc/lib"
//                cCompiler.args '-D_FILE_OFFSET_BITS=64'
//            }
//        }
//    }
//
//}
