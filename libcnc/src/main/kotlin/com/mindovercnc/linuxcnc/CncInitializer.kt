package com.mindovercnc.linuxcnc

object CncInitializer {
    fun initialize() {
        //System.loadLibrary("linuxcncini");
        System.loadLibrary("nml")
        System.loadLibrary("linuxcnchal")
        System.loadLibrary("LinuxCNC")
    }
}