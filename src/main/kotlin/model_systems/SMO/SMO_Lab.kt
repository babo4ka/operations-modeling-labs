package model_systems.SMO

import kotlin.math.pow

val getU: (Double) -> Double = {1/it}

val getP: (Double, Double) -> Double = {l, u -> l/u}

val getLSMO: (Double) -> Double = {it/(1-it)}

val getAvgW: (Double, Double) -> Double = {u, l -> 1/(u-l)}

val getP0: (Double) -> Double = {it.pow(0) * (1-it)}

fun main(){

    val l = 0.5
    val t = 2.0

    val u = getU(2.0)
    val p = getP(l, u)
    println("Интенсивность нагрузки : $p")

    val lsmo = getLSMO(p)
    println("Среднее количество судов в системе: $lsmo")
    val avgW = getAvgW(u, l)
    println("Среднее время ожидания: $avgW")
    val po = getP0(p)
    println("Вероятность пустой очереди: $po%")
}