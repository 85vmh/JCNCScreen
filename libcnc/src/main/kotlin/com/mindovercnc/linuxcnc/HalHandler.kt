package com.mindovercnc.linuxcnc

import com.mindovercnc.base.data.HalComponent

class HalHandler {
    external fun createComponent(name: String): HalComponent?
}