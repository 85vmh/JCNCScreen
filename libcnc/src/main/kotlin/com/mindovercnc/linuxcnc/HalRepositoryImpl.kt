package com.mindovercnc.linuxcnc

import com.mindovercnc.base.HalRepository

class HalRepositoryImpl : HalRepository {
    private val halHandler = HalHandler()

    override fun createComponent(name: String): Int {
        return halHandler.createComponent(name)
    }
}