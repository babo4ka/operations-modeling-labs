package simplexLab

import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.api.column
import org.jetbrains.kotlinx.dataframe.api.columnOf
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.api.print

fun main() {
    var names = columnOf("Кожа", "Каблуки", "Стельки", "Подошвы", "Цена")
    names = names.rename("Комплектующие")

    val sapogiArr = mutableListOf<Number>(0.2, 0.5, 0.027, 0.1, 900)
    var sapogi = column(sapogiArr)
    sapogi = sapogi.rename("Сапоги")

    val botinkiArr = mutableListOf<Number>(0.3, 0.2, 0.022, 0.25, 500)
    var botinki = column(botinkiArr)
    botinki = botinki.rename("Ботинки")

    val botilioniArr = mutableListOf<Number>(0.32, 0.45, 0.21, 0.28, 700)
    var botilioni = column(botilioniArr)
    botilioni = botilioni.rename("Ботильоны")

    val zapasiArr = mutableListOf<Number>(50, 70, 80, 40, Double.NaN)
    var zapasi = column(zapasiArr)
    zapasi = zapasi.rename("Запасы")

    val df = dataFrameOf(names, sapogi, botinki, botilioni, zapasi)

    df.print()


    println("МАТЕМАТИЧЕСКАЯ МОДЕЛЬ")
    println("===================================")
    for(i in 0..<df.rowsCount()-1){
        println("${df[i]["Сапоги"]}*x1 + ${df[i]["Ботинки"]}*x2 ${df[i]["Ботильоны"]}*x3 <= ${df[i]["Запасы"]}")
    }
    println("x1, x2, x3 >= 0")

    println()

    println("С ДОПОЛНИТЕЛЬНЫМИ ПЕРЕМЕННЫМИ")
    println("===================================")
    val extra = arrayOf("x4", "x5", "x6", "x7")
    for(i in 0..<df.rowsCount()-1){
        println("${df[i]["Сапоги"]}*x1 + ${df[i]["Ботинки"]}*x2 ${df[i]["Ботильоны"]}*x3 + ${extra[i]} = ${df[i]["Запасы"]}")
    }
    println("x1, x2, x3, x4, x5, x6, x7 >= 0")

    val ssd = initialDataFrame(sapogiArr, botinkiArr, botilioniArr, zapasiArr)
    ssd.print()
}

fun initialDataFrame(x1:MutableList<Number>, x2:MutableList<Number>, x3:MutableList<Number>, right:MutableList<Number>):AnyFrame{
    val extra = columnOf("x4", "x5", "x6", "x7", "f")
    right[right.lastIndex] = 0

    return dataFrameOf(extra, column(x1).rename("x1"), column(x2).rename("x2"), column(x3).rename("x3"), column(right))
}

fun createDataFrame(){

}