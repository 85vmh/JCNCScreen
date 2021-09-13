package com.mindovercnc.linuxcnc

import com.mindovercnc.base.Initializer

object CncInitializer : Initializer{
    override fun initialize() {
        //System.loadLibrary("linuxcncini");
        System.loadLibrary("nml")
        System.loadLibrary("linuxcnchal")
        System.loadLibrary("LinuxCNC")
    }
}