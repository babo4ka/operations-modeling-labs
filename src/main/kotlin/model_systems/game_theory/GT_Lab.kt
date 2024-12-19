package model_systems.game_theory


fun findLowerPrice(a:Array<IntArray>):Int{
    return a.maxOf { it.min() }
}

fun findHigherPrice(a:Array<IntArray>):Int{
    val cols = a[0].indices

    return cols.minOf{ a.maxOf { el -> el[it] } }
}


fun findSedlo(a:Array<IntArray>):Pair<Boolean, Pair<Int, Int>>{

    a.forEachIndexed{id, it ->
        val maxInRow = it.max()
        val col = it.indexOf(maxInRow)

        val minInCol = a.minOf { el -> el[col]}

        if(maxInRow == minInCol){
            return true to (id to col)
        }
    }

    return false to (-1 to -1)
}


fun main(){
    val a = arrayOf(
        intArrayOf(1, 4, 6, 5),
        intArrayOf(5, 6, 3, 9),
        intArrayOf(7, 3, 5, 4)
    )

    val lower = findLowerPrice(a)
    val higher = findHigherPrice(a)

    println("Нижняя цена: $lower")
    println("Верхняя цена: $higher")


    val(found, coord) = findSedlo(a)

    if(found){
        println("Седловая точка найдена: ${coord.first} ${coord.second}")
    }else{
        println("Седловой точки нет")
    }
}