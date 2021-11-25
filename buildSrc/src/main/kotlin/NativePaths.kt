import org.gradle.api.Project
import java.io.File

object NativePaths {
    fun getNativePaths(rootProject: Project): List<String> {
        val nativePath = File(rootProject.projectDir, "libcnc/native")
        return listOf(
            nativePath.path,
            File(nativePath, "lc/lib").path,
            "/home/vasimihalca/.jdks/openjdk-17.0.1/lib",
            "/usr/lib/x86_64-linux-gnu/java/vtk-Linux-x86_64"
        )
    }
}