package com.mindovercnc.linuxcnc

object CncInitializer {
    operator fun invoke() {
        //System.loadLibrary("linuxcncini");
        System.loadLibrary("nml")
        System.loadLibrary("linuxcnchal")
        System.loadLibrary("LinuxCNC")
    }
}