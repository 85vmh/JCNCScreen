package extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.trimDigits(maxDigits: Int = 3): String {
    return BigDecimal(this).setScale(maxDigits, RoundingMode.HALF_EVEN).toString()
}

fun Double.stripZeros(maxDigits: Int = 3): String {
    return BigDecimal(this).setScale(maxDigits, RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()
}