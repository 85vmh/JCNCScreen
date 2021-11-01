package com.mindovercnc.base

interface HalRepository {
    fun createComponent(name: String): Int
}