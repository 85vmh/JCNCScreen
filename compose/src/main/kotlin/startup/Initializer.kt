package startup

import app.Files
import com.mindovercnc.database.DbInitializer
import com.mindovercnc.linuxcnc.CncInitializer

object Initializer {

    operator fun invoke(startupArgs: StartupArgs) {
        CncInitializer(Files.appDir)

        DbInitializer()
    }
}