package model_operations.inventoryManage

import kotlin.math.sqrt


fun main(){


    val getOptimalOrderSize: (Double, Double, Double) -> Double = { k, v, s ->
        sqrt(((2 * k * v)  / s))
    }

    val spendsForWeek: (Double, Double, Double, Double) -> Double = {k, v, q, s ->
        (k * (v / q)) + (s * (q / 2))
    }

    val countPeriods: (Double, Double) -> Double = {q, v ->
        q/v
    }


    val orderValue = 300.0
    val fixedOrderPrice = 20.0
    val priceForWeekPerOneFt = 0.03 * 7
    val intensity = orderValue / 1
    val fullOrderPrice = priceForWeekPerOneFt * (orderValue/2) + fixedOrderPrice

    val Qw = getOptimalOrderSize(fixedOrderPrice, intensity, priceForWeekPerOneFt)
    println(Qw)

    val L = spendsForWeek(fixedOrderPrice, intensity, Qw, priceForWeekPerOneFt)
    println(L)

    val t = countPeriods(Qw, intensity)
    println(t)


    val difference = fullOrderPrice - L
    println("$fullOrderPrice - $L = $difference")
}