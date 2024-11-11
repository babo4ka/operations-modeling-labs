package model_systems

import org.jetbrains.kotlinx.dataframe.math.mean
import kotlin.math.pow
import kotlin.math.sqrt

val x = listOf(15, 14, 12, 10, 9, 8, 7, 5)
val y = listOf(150, 145, 120, 100, 95, 75, 70, 55)

//val x = listOf(12.85, 12.32, 11.43, 10.59, 10.21, 9.65, 9.63, 9.22)
//val y = listOf(154.77, 145.59, 108.37, 100.76, 98.32, 81.43, 80.97, 79.04)


val avgX = x.mean()
val avgY = y.mean()

val sumOfMulsWithAvg = x.foldIndexed(0.0) { id, acc, el ->
    (el - avgX) * (y[id] - avgY) + acc
}

val sumOfSquaresX = x.fold(0.0) { acc, el ->
    (el - avgX).pow(2.0) + acc
}


val sumOfSquaresY = y.fold(0.0) { acc, el ->
    (el - avgY).pow(2.0) + acc
}

val q = sumOfMulsWithAvg / (sqrt(sumOfSquaresX) * sqrt(sumOfSquaresY))

val sumOfXSquares = x.fold(0.0){acc, el ->
    el.toDouble().pow(2.0) + acc
}

val sumOfMuls = x.foldIndexed(0.0) {id, acc, el ->
    acc + (el * y[id])
}

val delta = (x.size * sumOfXSquares) - (x.sum() * x.sum())

val delta1 = (y.sum() * sumOfXSquares) - (x.sum() * sumOfMuls)

val delta2 = (x.size * sumOfMuls) - (y.sum() * x.sum())

val a1 = delta1 / delta
val a2 = delta2 / delta

val yT = x.map { el -> a1 + (a2 * el)}

val sumOfSquaresYT = y.foldIndexed(0.0) {id, acc, el ->
    acc + (el - yT[id]).pow(2.0)
}

val r = 1 - (sumOfSquaresYT / sumOfSquaresY)

fun main(){
    println(q)

    println(r)
}