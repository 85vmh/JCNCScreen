package com.mindovercnc.base.data

data class HalPin<T> private constructor(val name: String, val type: Type, val dir: Dir) {

    var value : T? = null

    enum class Type(val value: Int) {
        TYPE_UNSPECIFIED(-1),
        TYPE_UNINITIALIZED(0),
        BIT(1),
        FLOAT(2),
        S32(3),
        U32(4),
        PORT(5);
    }

    enum class Dir(val value: Int) {
        DIR_UNSPECIFIED(-1), IN(16), OUT(32), IO(16 or 32);
    }

    companion object{
        fun bit(name: String, dir: Dir) = HalPin<Boolean>(name, Type.BIT, dir)
        fun float(name: String, dir: Dir) = HalPin<Float>(name, Type.FLOAT, dir)
        fun s32(name: String, dir: Dir) = HalPin<Short>(name, Type.S32, dir)
        fun u32(name: String, dir: Dir) = HalPin<UInt>(name, Type.U32, dir)
    }
}