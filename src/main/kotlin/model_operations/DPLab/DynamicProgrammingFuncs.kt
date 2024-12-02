package model_operations.DPLab


fun fillTable(m:IntArray, c:IntArray, p:Int):Array<IntArray>{
    val n = m.size

    val table = Array(n+1){IntArray(p+1)}

    table[0] = IntArray(p+1){0}

    table.forEach { it[0] = 0 }

    for(i in 1..n){
        for(j in 1..p){
            if(m[i-1] > j){
                table[i][j] = table[i-1][j]
            }else{
                val s = maxOf(table[i-1][j], table[i-1][j-m[i-1]]+c[i-1])
                table[i][j] = s

//                printTable(table)
//                println()
            }

            continue
        }
    }

    println(table[n][p])

    return table
}

fun backStep(table:Array<IntArray>, m:IntArray, p:Int):IntArray{
    val n = m.size

    var w = p
    val selectedItems = mutableListOf<Int>()

    for(i in n downTo 1){
        if(table[i][w] != table[i-1][w]){
            selectedItems.add(i)

            w-= m[i-1]
        }
    }


    return selectedItems.toIntArray()
}


fun printTable(table:Array<IntArray>){

    fun getLength():Int{
        var max = 0

        table.forEach {
            val m = it.max()
            if(m > max) max = m
        }

        return max.toString().length + 1
    }


    val maxL = getLength()

    table.forEach {
        it.forEach { el ->
            print("$el${" ".repeat(if(maxL > el.toString().length) maxL - el.toString().length else 0)}|")
        }

        println()
    }
}


fun main(){

    val m = intArrayOf(4, 5, 3, 7, 6)
    val c = intArrayOf(5, 7, 4, 9, 8)
    val p = 15


    val table = fillTable(m, c, p)

    printTable(table)

    val items = backStep(table, m, p)

    println(items.joinToString(" "))
}