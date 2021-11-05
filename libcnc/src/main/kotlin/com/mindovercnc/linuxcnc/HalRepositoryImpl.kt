package com.mindovercnc.linuxcnc

import com.mindovercnc.base.HalRepository
import com.mindovercnc.base.data.HalComponent

class HalRepositoryImpl : HalRepository {
    private val halHandler = HalHandler()

    override fun createComponent(name: String): HalComponent? {
        return halHandler.createComponent(name)
    }
}