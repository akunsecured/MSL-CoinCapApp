package hu.bme.aut.msl_coincapapp.utils

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(decimals: Int): Double =
    (this * (10.0).pow(decimals)).roundToInt() / (10.0).pow(decimals)