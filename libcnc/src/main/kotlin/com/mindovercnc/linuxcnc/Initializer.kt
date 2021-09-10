package com.mindovercnc.linuxcnc

object Initializer {
    fun loadLibraries() {
        //System.loadLibrary("linuxcncini");
        System.loadLibrary("nml")
        System.loadLibrary("linuxcnchal")
        System.loadLibrary("LinuxCNC")
    }
}