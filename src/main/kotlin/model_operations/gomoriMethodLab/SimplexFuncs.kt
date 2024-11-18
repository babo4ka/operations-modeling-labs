package model_operations.gomoriMethodLab

import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.truncate


fun createSimplexTable(z:DoubleArray, a:Array<Pair<DoubleArray, String>>, b:DoubleArray):Pair<Array<DoubleArray>, Int>{

    val m = a.size // количество ограничений
    val n = z.size // количество переменных

    val rowsWithArtificial = a.filter { it.second == ">=" }
    val colsCount = rowsWithArtificial.size

    var mainFunVal = 0.0

    val table = Array(m+1) {DoubleArray(n + colsCount + 1)}

    var artIndex = 0

    val artIndexes = mutableListOf<Int>()

    for(i in 0..<m){
        for(j in 0..<n){
            table[i][j] = a[i].first[j]
        }

        table[i][table[i].lastIndex] = b[i]

        if(a[i].second == ">="){
            table[i][n+artIndex] = -1.0
            table.last()[n+artIndex] = -1.0
            artIndex++
            mainFunVal += b[i]

            artIndexes.add(i)
        }
    }

    for(i in 0..<z.size){
        table[m][i] = -(z[i] - artIndexes.fold(0.0) {acc, el -> a[el].first[i] + acc})
    }

    table.last()[table[0].lastIndex] = mainFunVal

    return table to artIndex
}

fun chooseColumn(a:Array<DoubleArray>, goal:String = "min", zCount:Int):Int{
    val zCol = a.last().slice(0..<zCount)
    return if(goal == "min") zCol.toList().indexOf(zCol.min()) else zCol.toList().indexOf(zCol.max())
}

fun chooseColumnAfterRow(a:Array<DoubleArray>, row:Int):Int{
    var minDel = Double.MAX_VALUE
    var minDelCol = 0

    a[row].forEachIndexed {id, it ->
        if(it != 0.0 && id != a[row].lastIndex){
            val del = a[row].last() / abs(it)
            if(del < minDel){
                minDel = del
                minDelCol = id
            }
        }

    }

    return minDelCol
}

fun chooseRow(a:Array<DoubleArray>, b:DoubleArray, col:Int):Int{
    var minDel = Double.MAX_VALUE
    var minI = 0

    for(i in 0..<a.size-1){
        if(a[i][col] <= 0) continue

        if(b[i] / a[i][col] < minDel){
            minDel = b[i] / a[i][col]
            minI = i
        }
    }
    return minI
}

fun needToDoStep(z:DoubleArray, goal:String="min", zCount:Int):Boolean{
    return when(goal){
        "max" -> z.slice(0..<zCount).any { it > 0 }
        "min" -> z.slice(0..<zCount).any{ it < 0}
        else -> false
    }
}

fun makeSimplexStep(a:Array<DoubleArray>, coord:Pair<Int,Int>):Array<DoubleArray>{
    val (row, col) = coord

    val pivot = a[row][col]

    val newTable = Array(a.size){DoubleArray(a[0].size)}

    newTable[row][col] = 1 / pivot

    for(i in 0..<a[0].size){
        if(i != col) newTable[row][i] = a[row][i] / pivot
    }

    for(i in 0..<a.size){
        if(i != row) newTable[i][col] = -(a[i][col] / pivot)
    }

    for(i in 0..<a.size){
        for(j in 0..<a[0].size){
            if(i == row || j == col) continue

            val mainDMul = a[i][j] * pivot
            val secDMul = a[i][col] * a[row][j]
            val newVal = (mainDMul - secDMul) / pivot
            newTable[i][j] = if(newVal == 0.0) 0.0 else newVal
        }
    }

    return newTable
}

fun createNewConstraint(a: Array<DoubleArray>, basisIndices:MutableList<Int>):Array<DoubleArray>{
    val b = a.slice(a.indices).map { it.last() }
    var minDecRow = 0
    var minDec = Double.MAX_VALUE

    b.forEachIndexed { id, it ->
        if(basisIndices.contains(id)){
            val dec = it - truncate(it)
            if(dec < minDec && it != 0.0){
                minDec = dec
                minDecRow = id
            }
        }
    }

    val newRow = a[minDecRow].map{
        if(it == 0.0){
            0.0
        }else if(it > 0){
            -(it - truncate(it))
        }else{
            -(it - truncate(it)) - 1.0
        }

    }


    return a.copyOfRange(0, a.lastIndex).plus(newRow.toDoubleArray()) + a.copyOfRange(a.size-1, a.size)
}

fun sliceTable(a:Array<DoubleArray>, count:Int):Array<DoubleArray>{
    return a.map{
        it.slice(count..<it.size).toDoubleArray()
    }.toTypedArray()
}

fun isAllBIntegers(a:Array<DoubleArray>, basisIndices:MutableList<Int>):Boolean{
    return a.filterIndexed { id, _ -> basisIndices.contains(id) }.all { (makeInt(it.last()) - makeInt(truncate(it.last()))) == 0.0 }
}


fun isInConstraints(constraints:Array<Pair<DoubleArray, String>>, answer:MutableMap<String, Int>, b:DoubleArray):Boolean{

    val allData = constraints.mapIndexed { i, it ->
        val (cons, sign) = it
        val sum = cons.foldIndexed(0.0) { id, acc, el ->
            answer["x${id+1}"]!! * el + acc
            }

        when(sign){
            ">=" -> sum >= b[i]
            "<=" -> sum <= b[i]
            else -> false
        }
   }


    return allData.all { it }
}

fun main(){
    val a = arrayOf(
        Pair(doubleArrayOf(0.0, 2.0), ">="),
        Pair(doubleArrayOf(2.0, -3.0), "<="),
        Pair(doubleArrayOf(4.0, 3.0), ">="),
        Pair(doubleArrayOf(-3.0, 1.0), "<=")
    )

    val b = doubleArrayOf(5.0, 7.0, 8.0, 8.0)

    val z = doubleArrayOf(3.0, 2.0)

    val freeVars = mutableMapOf<String, Int>()
    val basisVars = mutableMapOf<String, Int>()
    z.forEachIndexed{ id, _ -> freeVars["x${id+1}"] = id}

    var (table, artCount) = createSimplexTable(z, a, b)

    println("Начальная симплекс-таблица")
    table.forEach {
        println(it.joinToString(" "))
    }



    while(needToDoStep(table.last(), "max", z.size)){
        val col = chooseColumn(table, "max", z.size)
        val row = chooseRow(table, b, col)

        freeVars.keys.forEach{
            if(freeVars[it] == col){
                basisVars[it] = row
                freeVars.remove(it)
            }
        }
        println(row to col)
        table = makeSimplexStep(table, (row to col))

        println()
        table.forEach {
            println(it.joinToString(" "))
        }
    }

    println("Решённая задача без условия целочисленности")
    table.forEach {
        println(it.joinToString(" "))
    }

    table = sliceTable(table, artCount)

    println("Обрезанная таблица")
    table.forEach {
        println(it.joinToString(" "))
    }

    var i = 0

    while(!isAllBIntegers(table, basisVars.values.toMutableList())){
        table = createNewConstraint(table, basisVars.values.toMutableList())
        val row = table.size-2
        val col = chooseColumnAfterRow(table, row)
        println()
        table.forEach {
            println(it.joinToString(" "))
        }
        println(row to col)


        table = makeSimplexStep(table, row to col)
        println()
        table.forEach {
            println(it.joinToString(" "))
        }
    }

    for(i in table.indices){
        for(j in table[0].indices){
            table[i][j] = table[i][j].toBigDecimal().setScale(0, RoundingMode.HALF_UP).toDouble()
        }
    }

    table.forEach {
        println(it.joinToString(" "))
    }

    val answer = mutableMapOf<String, Int>()

    basisVars.keys.forEach{
        answer[it] = table[basisVars[it]!!].last().toInt()
    }

    answer.keys.forEach {
        println("$it = ${answer[it]}")
    }

    println(isInConstraints(a, answer, b))
}