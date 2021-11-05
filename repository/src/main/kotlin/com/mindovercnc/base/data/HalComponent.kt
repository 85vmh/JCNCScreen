package com.mindovercnc.base.data

class HalComponent internal constructor(var name: String) {
    var componentId = 0

    external fun addPin(halPin: HalPin<*>) : Int

    external fun getPin(pinName: String): HalPin<*>

    override fun toString(): String {
        return "HalComponent{" +
                "componentId=" + componentId +
                ", name='" + name + '\'' +
                '}'
    }
}