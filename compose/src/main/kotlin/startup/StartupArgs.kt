package startup

import com.mindovercnc.linuxcnc.IniFilePath

data class StartupArgs(
    val iniFilePath: IniFilePath,
    val vtkEnabled: VtkEnabled,
    val topBarEnabled: TopBarEnabled
)