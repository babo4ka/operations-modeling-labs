package transportTaskLab


const val stockWord = "Запасы"
const val needsWord = "Потребность"

fun printData(tc:TableClass, withPlan:Boolean = false){
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


fun improvePlan(tc:TableClass, planMatrix:MutableList<MutableList<Int>>) : Pair<IntArray, IntArray>{
    val m = planMatrix.size
    val n = planMatrix[0].size

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

private fun printRepeatedly(str:String, length:Int, newLine:Boolean = true,
                            before:String = "", after:String = ""){
    if(newLine) println("$before${str.repeat(length)}$after")
    else print("$before${str.repeat(length)}$after")
}