package model_operations.inventoryManage

import kotlin.math.sqrt


fun main(){

    fun spendsOnWeek(){

    }

    val optimalOrderSize: (Double, Double, Double) -> Double = {k ,v ,s ->
        sqrt((2 * k * v) )/ s
    }

    val spendsForWeek: (Double, Double, Double, Double) -> Double = {k, v, q, s ->
        (k * (v / q)) + (s * (q / 2))
    }


    val orderValue = 300.0
    val fixedOrderPrice = 20.0
    val priceForWeekPerOneFt = 0.03 * 7
    val intensity = orderValue / 1
    val fullOrderPrice = priceForWeekPerOneFt * (orderValue/2) + fixedOrderPrice

    val Qw = optimalOrderSize(fixedOrderPrice, intensity, priceForWeekPerOneFt)
    println(Qw)

}