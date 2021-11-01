package com.mindovercnc.linuxcnc

class HalHandler {
    external fun createComponent(name: String): Int
    external fun addPin(halPin: Pin?, componentId: Int)
    external fun getPin(name: String?, componentId: Int): Pin?
    external fun setReady(componentId: Int)

    inner class Pin {
        var name: String? = null
    }
}


/**
 *
 * net spindle_signal   placa_mesa_input-01 =>  placa_mesa_output-01
 *
 *
 *
 * net spindle_lever_down  placa_mesa_input-01 => weilercomp.spindle-fwd
 *
 * net spindle_on   weilercomp.turn-on-sp  => placa_mesa_output-01
 *
 *
 *
 *
 */