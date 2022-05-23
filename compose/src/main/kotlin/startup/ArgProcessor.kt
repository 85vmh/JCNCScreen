package startup

import com.mindovercnc.linuxcnc.IniFilePath
import com.mindovercnc.linuxcnc.LinuxCncHome
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.io.File

object ArgProcessor {
    fun process(args: Array<String>): StartupArgs {
        val parser = ArgParser("KTCNC")

        val vtkEnabled by parser.option(
            ArgType.Boolean,
            shortName = "v",
            fullName = "vtk-enabled",
            description = "Set vtk enabled"
        ).default(true)

        val topBarEnabled by parser.option(
            ArgType.Boolean,
            shortName = "tb",
            fullName = "topbar-enabled",
            description = "Set topbar enabled"
        ).default(false)

        val iniPath by parser.argument(
            ArgType.String,
            description = "INI file path to load"
        )

        parser.parse(args)

        val iniFile = createIniFile(iniPath)
        if (!iniFile.exists()) {
            throw IllegalArgumentException("$iniPath does not exist")
        }

        return StartupArgs(
            IniFilePath(iniFile),
            VtkEnabled(vtkEnabled),
            TopBarEnabled(topBarEnabled)
        )
    }

    private fun createIniFile(path: String): File {
        var file = File(path)
        if (file.exists()) return file
        if (file.isAbsolute) throw IllegalArgumentException("${file.absolutePath} does not exist")

        //try finding it in linuxcnc folder
        file = File(LinuxCncHome, path)
        if (!file.exists()) throw IllegalArgumentException("${file.absolutePath} does not exist")
        return file
    }
}

@JvmInline
value class VtkEnabled(val enabled: Boolean)

@JvmInline
value class TopBarEnabled(val enabled: Boolean)