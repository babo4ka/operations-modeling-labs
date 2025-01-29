package model_operations.inventoryManage

import java.math.RoundingMode
import kotlin.math.sqrt


fun main(){

    val round:(Double) -> Double = {num ->
        num.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }


    val getOptimalOrderSize: (Double, Double, Double) -> Double = { fixedOrderPrice, intensity, priceForWeekPerOneFt ->
        sqrt(((2 * fixedOrderPrice * intensity)  / priceForWeekPerOneFt))
    }

    val countSpendsForWeek: (Double, Double, Double, Double) -> Double = { fixedOrderPrice, intensity, orderSize, priceForWeekPerOneFt ->
        (fixedOrderPrice * (intensity / orderSize)) + (priceForWeekPerOneFt * (orderSize / 2))
    }

//    val countPeriods: (Double, Double) -> Double = {orderSize, intensity ->
//        orderSize/intensity
//    }


    val orderValue = 300.0
    val fixedOrderPrice = 20.0
    val priceForWeekPerOneFt = 0.03 * 7
    val intensity = orderValue / 1
    val fullOrderPrice = priceForWeekPerOneFt * (orderValue/2) + fixedOrderPrice

    println("Текущие недельные затраты: ${round(fullOrderPrice)}")

    val optimalOrderSize = getOptimalOrderSize(fixedOrderPrice, intensity, priceForWeekPerOneFt)
    println("Оптимальный размер заказа: ${round(optimalOrderSize)}")

    val spendsForWeek = countSpendsForWeek(fixedOrderPrice, intensity, optimalOrderSize, priceForWeekPerOneFt)
    println("Недельные затраты при оптимальном заказе: ${round(spendsForWeek)}")

//    val periods = countPeriods(optimalOrderSize, intensity)
//    println(round(periods))


    val difference = fullOrderPrice - spendsForWeek
    println("Разница между текущими и оптимальными затратами: ${round(fullOrderPrice)} - ${round(spendsForWeek)} = ${round(difference)}")
}