package app

import java.io.File

object Files {

    private val homeFile = File(System.getProperty("user.home"))

    val appDir = File(homeFile, ".ktcnc").apply {
        mkdirs()
    }

}