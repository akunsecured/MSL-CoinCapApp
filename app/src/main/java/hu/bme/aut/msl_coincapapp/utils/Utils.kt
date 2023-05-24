package hu.bme.aut.msl_coincapapp.utils

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(decimals: Int): Double =
    (this * (10.0).pow(decimals)).roundToInt() / (10.0).pow(decimals)

fun Double.format(): String {
    val n: Int
    val unit: String

    if (this >= (10.0).pow(9)) {
        n = 9
        unit = "b"
    } else if (this >= (10.0).pow(6)) {
        n = 6
        unit = "m"
    } else if (this >= (10.0).pow(3)) {
        n = 3
        unit = "k"
    } else {
        n = 0
        unit = ""
    }

    if (n == 0) {
        return "$this"
    }

    return "${(this / (10.0).pow(n)).roundTo(2)}$unit"
}