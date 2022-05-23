import org.gradle.api.Project
import java.io.File

object NativePaths {
    fun getNativePaths(rootProject: Project): List<String> {
        val linuxCncHome = System.getenv("LINUXCNC_HOME")
            ?: throw IllegalStateException("LINUXCNC_HOME not set")
        val linuxCncJdk = System.getenv("LINUXCNC_JDK")
            ?: throw IllegalStateException("LINUXCNC_JDK not set")
        return listOf(
            File(linuxCncHome, "lib").path,
            File(linuxCncJdk, "lib").path,
            "/usr/lib"
//            "/usr/lib/x86_64-linux-gnu/java/vtk-Linux-x86_64"
        )
    }
}