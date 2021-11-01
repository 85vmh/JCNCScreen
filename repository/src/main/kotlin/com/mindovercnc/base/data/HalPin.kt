package com.mindovercnc.base.data

data class HalPin(
    var name: String,
    var type: Type,
    var dir: Dir
) {
    enum class Type {
        BIT, FLOAT, S32, U32
    }

    enum class Dir {
        IN, OUT, IO
    }
}