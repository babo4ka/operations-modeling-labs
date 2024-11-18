package model_operations.gomoriMethodLab

import java.math.RoundingMode
import kotlin.math.truncate

fun makeInt(a:Double):Double{
    val decimaled = a.toBigDecimal().setScale(3, RoundingMode.HALF_UP).toDouble()
    if(decimaled - truncate(decimaled) == .999 || decimaled - truncate(decimaled) == .000){
        return decimaled.toBigDecimal().setScale(0, RoundingMode.HALF_UP).toDouble()
    }

    return a
}