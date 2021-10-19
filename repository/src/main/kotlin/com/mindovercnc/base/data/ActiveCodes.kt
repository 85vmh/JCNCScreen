package com.mindovercnc.base.data

data class ActiveCodes(
    val gCodes: List<Float>,
    val mCodes: List<Float>
) {
    fun <T : ActiveCode> translateCode(values: Array<T>): T? {
        gCodes.forEach { code->
            val found = values.find { it.value == code }
            if(found!=null)
                return found
        }
        return null
    }
}

fun smthx(codes: ActiveCodes){
    val distanceMode = codes.translateCode(DistanceMode.values())
}

interface ActiveCode {
    val value: Float
}

enum class DistanceMode(override val value: Float, val strKey: String) : ActiveCode {
    ABSOLUTE(90f, "keyAbsoluteMode"),
    RELATIVE(91f, "keyRelativeMode")
}
