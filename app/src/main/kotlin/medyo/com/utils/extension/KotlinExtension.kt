package medyo.com.utils.utils.extension

import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.log10


val emptyString = ""
val zero = 0
val zeroL = 0L
val zeroF = 0f
val zeroD = 0.0


fun Int?.orZero() = this ?: 0

fun Long?.orZero() = this ?: 0L

fun Float?.orZero() = this ?: 0f

fun Double?.orZero() = this ?: 0.0

fun String?.orEmpty() = this ?: ""

fun Boolean?.orFalse() = this ?: false


fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

infix fun Int.quotient(divisor: Int): Int = this.floorDiv(divisor)

fun Int.toIndianFormatCustom(): String {
    val formatter = DecimalFormat("#,##,###")
    return formatter.format(this)
}