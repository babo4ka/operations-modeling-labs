package transportTaskLab

import kotlin.math.abs
import kotlin.math.min


const val stockWord = "Запасы"
const val needsWord = "Потребность"

fun printData(tc:TableClass, planMatrix:MutableList<MutableList<Int>>? = null){
    val lengths = mutableListOf<Int>()
    val tosMaxLength = mutableMapOf<Int, Int>()
    (tc.tos + mutableListOf(stockWord)).forEachIndexed { i, it ->
        lengths.add(it.length)
        tosMaxLength[i] = it.length
    }

    val fromsMaxLength = (tc.froms + mutableListOf(needsWord)).maxBy { it.length }.length

    val length = lengths.fold(fromsMaxLength) { acc, len ->
        acc + (len + 2) }

    printRepeatedly("=", length = length)
    printRepeatedly(" ", length = fromsMaxLength+1, after = "|", newLine = false)

    for(to in tc.tos){
        print("$to |")
    }
    print(stockWord)

    println()
    printRepeatedly("=", length = length)


    for(i in tc.tariffMatrix.indices){
        print("${tc.froms[i]} ${" ".repeat(fromsMaxLength - tc.froms[i].length)}|")

        tc.tariffMatrix[i].forEachIndexed{ id, it ->
            print("$it ${" ".repeat(tosMaxLength[id]!! - it.toString().length)}|")
        }

        print("${tc.stocks[i]} ${" ".repeat(tosMaxLength[tc.tos.lastIndex+1]!! - tc.stocks[i].toString().length)}")

        println()
        printRepeatedly("=", length = length)
    }

    print("$needsWord ${" ".repeat(fromsMaxLength - needsWord.length)}|")

    for(needsId in tc.needs.indices){
        print("${tc.needs[needsId]} ${" ".repeat(tosMaxLength[needsId]!! - tc.needs[needsId].toString().length)}|")
    }

    print(tc.stocksAndNeedsSum)
}

fun createInitialPlan(tc:TableClass): MutableList<MutableList<Int>>{

    val planMatrix = MutableList(tc.froms.size){MutableList(tc.tos.size){-1} }

    val tempNeeds = tc.needs.toMutableList()
    val tempStocks = tc.stocks.toMutableList()
    var needsId = 0
    var stocksId = 0

    while(needsId < tempNeeds.size && stocksId < tempStocks.size){
        val minimalVal = minOf(tempNeeds[needsId], tempStocks[stocksId])

        tempNeeds[needsId] -= minimalVal
        tempStocks[stocksId] -= minimalVal

        planMatrix[stocksId][needsId] = minimalVal

        if(tempNeeds[needsId] == 0 && tempStocks[stocksId] == 0){
            if(stocksId < tempStocks.size -1){
                planMatrix[needsId][stocksId+1] = 0
            }else if(needsId < tempNeeds.size - 1){
                planMatrix[needsId+1][stocksId] = 0
            }
        }

        if(tempNeeds[needsId] == 0) needsId++
        if(tempStocks[stocksId] == 0) stocksId++
    }

    return planMatrix
}


fun createPotentials(tc:TableClass, planMatrix:MutableList<MutableList<Int>>,
                     m:Int = planMatrix.size, n:Int = planMatrix[0].size) : Pair<IntArray, IntArray>{
    val u = IntArray(m){Int.MAX_VALUE}
    val v = IntArray(n){Int.MAX_VALUE}

    u[0] = 0

    var changed = true
    var a = 0
    while (changed) {
        changed = false

        for (i in 0..<m) {
            for (j in 0..<n) {
                if (planMatrix[i][j] >= 0) {
                    if (u[i] != Int.MAX_VALUE && v[j] == Int.MAX_VALUE) {
                        v[j] = tc.tariffMatrix[i][j] - u[i]
                        changed = true
                    } else if (v[j] != Int.MAX_VALUE && u[i] == Int.MAX_VALUE) {
                        u[i] = tc.tariffMatrix[i][j] - v[j]
                        changed = true
                    }
                }
            }
        }
        a++
    }

    return u to v
}

fun isOptimal(tc:TableClass, planMatrix:MutableList<MutableList<Int>>,
               u:IntArray, v:IntArray,
              m:Int = planMatrix.size, n:Int = planMatrix[0].size):Pair<Boolean, Pair<Int, Int>>{

    var optimal = true
    var maxVal = 0
    var max = Pair(0, 0)

    for(i in 0..<m){
        for(j in 0..<n){
            if((u[i] + v[j] > tc.tariffMatrix[i][j])){
                //println("sum ${u[i] + v[j]} cost ${tc.tariffMatrix[i][j]}  i $i j $j")
                optimal = false
                if(u[i] + v[j] > maxVal){
                    maxVal = u[i] + v[j]
                    max = Pair(i, j)
                }
            }
        }
    }

    return Pair(optimal, max)
}

fun improvePlan(//tc:TableClass,
                planMatrix:MutableList<MutableList<Int>>,
                //u:IntArray, v:IntArray,
                start:Pair<Int,Int>,
                m:Int = planMatrix.size, n:Int = planMatrix[0].size): MutableList<MutableList<Int>>{

    val cycle = mutableListOf(start)
    val marked = Array(m + n) {false}

//    outer@ for(i in 0..<m){
//        for(j in 0..<n){
//            if(planMatrix[i][j] >= 0){
//                cycle.add(i to j)
//                marked[i] = true
//                break@outer
//            }
//        }
//    }

//    for(i in cycle){
//        println(i)
//    }
//    println("^before")

    while(true){
        var found = false

        val currCycle = cycle.toList()

        for(cycleItem in currCycle){
            if(cycleItem.first < m-1){
                for(j in 0..<n){
                    if(!marked[m+j] && planMatrix[cycleItem.first][j] >= 0){
                        cycle.add(j to cycleItem.first)
                        marked[m + j] = true
                        found = true
                    }
                }
            }else{
                val rowId = abs( cycleItem.first - m)
                for(j in 0..<m){
                    if(!marked[j] && planMatrix[rowId][j] >= 0){
                        cycle.add(rowId to j)
                        marked[j] = true
                        found = true
                    }
                }
            }
        }



        if(!found) break
    }
    for(i in cycle){
        println(i)
    }


    val filtered = cycle.map {
        planMatrix[it.first][it.second]
    }.toMutableList()

    filtered.removeAll { it == -1 }

    val minTransport = filtered.minOrNull() ?: 0


    for(i in cycle.indices){
        val (x, y) = cycle[i]
        if(i % 2 == 0)
            planMatrix[x][y] -= minTransport
        else
            planMatrix[x][y] += minTransport
    }

    return planMatrix
}

private fun printRepeatedly(str:String, length:Int, newLine:Boolean = true,
                            before:String = "", after:String = ""){
    if(newLine) println("$before${str.repeat(length)}$after")
    else print("$before${str.repeat(length)}$after")
}