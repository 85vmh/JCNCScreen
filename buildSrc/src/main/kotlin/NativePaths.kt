import org.gradle.api.Project
import java.io.File

object NativePaths {
    fun getNativePaths(rootProject: Project): List<String> {
        val linuxCncHome = System.getenv("LINUXCNC_HOME")
            ?: throw IllegalStateException("LINUXCNC_HOME not set")
        return listOf(
            File(linuxCncHome, "lib").path,
            "/home/vasimihalca/.jdks/openjdk-17.0.1/lib",
            "/usr/lib"
//            "/usr/lib/x86_64-linux-gnu/java/vtk-Linux-x86_64"
        )
    }
}