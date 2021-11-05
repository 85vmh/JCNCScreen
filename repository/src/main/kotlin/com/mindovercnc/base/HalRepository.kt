package com.mindovercnc.base

import com.mindovercnc.base.data.HalComponent

interface HalRepository {
    fun createComponent(name: String): HalComponent?
}