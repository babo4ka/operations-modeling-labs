package transportTaskLab


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

fun improvePlan(planMatrix:MutableList<MutableList<Int>>,
                start:Pair<Int,Int>,
                m:Int = planMatrix.size, n:Int = planMatrix[0].size): MutableList<MutableList<Int>>{

    val cycle = mutableListOf(start)

    fun searchCycle(point:Pair<Int, Int>, searchBy:String = "row"):Boolean{
        val (row, col) = point

        if(searchBy == "row"){
            for(i in 0..<m){
                if((i to col) == start && (i to col) != point) {
                    cycle.add(point)
                    return true
                }
                if((i to col) != point && planMatrix[i][col] >= 0){
                    val isPart = searchCycle((i to col), "col")
                    if(isPart){
                        if(point != start) cycle.add(point)
                        return true
                    }
                }
            }
        }else if(searchBy == "col"){
            for(j in 0..<n){
                if((row to j) == start && (row to j) != point) {
                    cycle.add(point)
                    return true
                }
                if((row to j) != point && planMatrix[row][j] >= 0){
                    val isPart = searchCycle((row to j), "row")
                    if(isPart){
                        if(point != start) cycle.add(point)
                        return true
                    }
                }
            }
        }


        return false
    }

    searchCycle(start)

//    println("cycle: ")
//    for(i in cycle){
//        println(i)
//    }


    var filtered = cycle.mapIndexed {id, it ->
        planMatrix[it.first][it.second]
    }.toMutableList()


    filtered = filtered.filterIndexed { id, i -> id % 2 != 0 }.toMutableList()
    filtered.removeAll { it == -1 }

    val minTransport = filtered.minOrNull() ?: 0

    for(i in cycle.indices){
        val (x, y) = cycle[i]
        if(i % 2 == 0) {
            if(planMatrix[x][y] == -1 )planMatrix[x][y] += 1
            planMatrix[x][y] += minTransport

        } else {
            planMatrix[x][y] -= minTransport
            if(planMatrix[x][y] == 0) planMatrix[x][y] = -1
        }


    }


    return planMatrix
}

fun finalCost(planMatrix: MutableList<MutableList<Int>>, tc: TableClass):Int{
    var sum = 0
    val tariffMatrix = tc.tariffMatrix

    for (i in 0..<planMatrix.size){
        for(j in 0..<planMatrix[0].size){
            sum+= planMatrix[i][j] * tariffMatrix[i][j]
        }
    }
    return sum
}


fun printPlan(pm:MutableList<MutableList<Int>>){
    println("План перевозок: ")
    for(m in pm){
        println(m.joinToString(" "))
    }
}
private fun printRepeatedly(str:String, length:Int, newLine:Boolean = true,
                            before:String = "", after:String = ""){
    if(newLine) println("$before${str.repeat(length)}$after")
    else print("$before${str.repeat(length)}$after")
}