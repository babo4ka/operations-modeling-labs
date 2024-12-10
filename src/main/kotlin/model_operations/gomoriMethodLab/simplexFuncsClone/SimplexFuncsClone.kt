package model_operations.gomoriMethodLab.simplexFuncsClone

import model_operations.gomoriMethodLab.makeInt
import model_operations.gomoriMethodLab.printData
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.truncate


var freeVars = mutableMapOf<String, Int>()
val basisVars = mutableMapOf<String, Int>()
val needVars = mutableListOf<String>()


fun Z(z:DoubleArray, vars:DoubleArray):Double{

    return z.foldIndexed(0.0){id, acc, el ->
        acc + el * vars[id]
    }
}

fun createSimplexTable(z:DoubleArray, a:Array<Pair<DoubleArray, String>>, b:DoubleArray):Pair<Array<DoubleArray>, Int>{

    val m = a.size // количество ограничений
    val n = z.size // количество переменных

    val colsCount = a.filter { it.second == ">=" }.size

    var mainFunVal = 0.0

    val table = Array(m+1) {DoubleArray(n + m + 1)}

    var artIndex = 0

    val artIndexes = mutableListOf<Int>()

    var index = 0

    for(i in 0..<m){
        for(j in 0..<n){
            table[i][j] = a[i].first[j]
        }

        table[i][table[i].lastIndex] = b[i]

        val newNumOfVar = freeVars.size + 1

        basisVars["x$newNumOfVar"] = i
        freeVars["x$newNumOfVar"] = i + z.size

        table[i][index + n] = 1.0
        index++


    }

    for(i in 0..<z.size){
        table[m][i] = (z[i] - artIndexes.fold(0.0) {acc, el -> a[el].first[i] + acc})
    }

    table.last()[table[0].lastIndex] = mainFunVal

    return table to artIndex
}

fun chooseColumn(a:Array<DoubleArray>, goal:String = "min"):Int{
    val zCol = a.last().slice(0..<a.last().size-1)
    return if(goal == "min") zCol.toList().indexOf(zCol.max()) else zCol.toList().indexOf(zCol.min())
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
        if(a[i][col] == 0.0) continue
        if(b[i] > 0 && a[i][col] < 0) continue
        if(b[i] < 0 && a[i][col] > 0) continue

        if(b[i] / a[i][col] < minDel){
            minDel = b[i] / a[i][col]
            minI = i
        }
    }
    return minI
}

fun needToDoStep(z:DoubleArray, goal:String="min"):Boolean{
    return when(goal){
        "min" -> z.slice(0..<z.size-1).any{ it > 0 }
        "max" -> z.slice(0..<z.size-1).any{ it > 0 }
        else -> false
    }
}


fun needToDoPreStep(b:DoubleArray):Boolean{
    return b.any { it < 0.0 }
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

fun makePreStep(a:Array<DoubleArray>, coord:Pair<Int,Int>):Array<DoubleArray>{
    val newTable = Array(a.size){DoubleArray(a[0].size)}

    val (row, col) = coord
    val pivot = a[row][col]

    a[row].forEachIndexed {i, it->
        newTable[row][i] = it / pivot
    }

    a.forEachIndexed {i, it ->
        if(i != row){
            it.forEachIndexed { id, el ->
                newTable[i][id] = a[i][id] - newTable[row][id] * a[i][col]
            }
        }
    }


    return newTable
}

fun createNewConstraint(a: Array<DoubleArray>):Array<DoubleArray>{
    val b = a.slice(a.indices).map { it.last() }
    var minDecRow = 0
    var minDec = Double.MAX_VALUE

    b.forEachIndexed { id, it ->
        val dec = it - truncate(it)
        if(dec < minDec && it != 0.0 && dec != 0.0){
            minDec = dec
            minDecRow = id
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

    val newArr = Array(a.size + 1){DoubleArray(a[0].size+1)}


    for(i in 0..<a.size-1){
        newArr[i] = a[i].copyOfRange(0, a[i].lastIndex)
            .plus(0.0) + a[i].copyOfRange(a[i].size - 1, a[i].size)
    }

    newArr[newArr.size-2] = newRow.toDoubleArray().copyOfRange(0, newRow.lastIndex)
        .plus(1.0) + newRow.toDoubleArray().copyOfRange(newRow.size - 1, newRow.size)

    newArr[newArr.lastIndex] = a.last().copyOfRange(0, a.last().lastIndex)
        .plus(0.0) + a.last().copyOfRange(a.last().size - 1, a.last().size)


    val numOfNewVar = freeVars.size + 1

    basisVars["x$numOfNewVar"] = newArr.size - 2
    freeVars["x$numOfNewVar"] = newArr[0].size - 2


    return newArr
}

fun sliceTable(a:Array<DoubleArray>, count:Int):Array<DoubleArray>{

    var newMap = freeVars.filter { !(0..<count).contains(it.value) }.toMutableMap()
    var id = 0
    newMap = newMap.toList().sortedBy { (_, v) -> v }.toMap() as MutableMap<String, Int>
    newMap.keys.forEach {
        newMap[it] = id
        id++
    }
    freeVars = newMap

    return a.map{
        it.slice(count..<it.size).toDoubleArray()
    }.toTypedArray()
}

fun isAllBIntegers(a:Array<DoubleArray>):Boolean{
    return a.all { (makeInt(it.last()) - makeInt(it.last())) == 0.0 }
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

fun replaceVars(col:Int, row:Int){
    var keyToAdd = ""
    freeVars.keys.forEach{
        if(freeVars[it] == col){
            keyToAdd = it
        }
    }

    var keyToCh = ""
    basisVars.keys.forEach {
        if(basisVars[it] == row){
            keyToCh = it
        }
    }

    if(keyToCh != "") {
        basisVars.remove(keyToCh)
    }

    if(keyToAdd!= ""){
        basisVars[keyToAdd] = row
    }
}

fun makeminus(a:Array<Pair<DoubleArray, String>>, b:DoubleArray):
        Pair<Array<Pair<DoubleArray, String>>, DoubleArray>{
    val a1 = mutableListOf<Pair<DoubleArray, String>>()
    val b1 = DoubleArray(b.size)
    a.forEachIndexed {id, it ->
        if(it.second == "<=") {
            a1.add(it)
            b1[id] = b[id]
        }
        else{
            val newArr = it.first.map { if(it != 0.0) -it else 0.0 }.toDoubleArray()
            a1.add(newArr to "<=")
            b1[id] = -b[id]
        }
    }

    return a1.toTypedArray() to b1
}

fun countDeltas(a:Array<DoubleArray>, z:DoubleArray):Array<DoubleArray>{

    val newZ = DoubleArray(a.last().size)

    a.last().forEachIndexed{ i, _ ->
        val sum = needVars.foldIndexed(0.0) {id, acc, it ->
            if(basisVars.keys.contains(it)){
                a[basisVars[it]!!][i] * z[id] + acc
            }else{
                acc
            }
        }

        newZ[i] = sum - if(i>z.lastIndex) 0.0 else z[i]
    }

    a[a.lastIndex] = newZ

    return a
}

fun main(){
    val (a, b) = makeminus(arrayOf(
        Pair(doubleArrayOf(0.0, 2.0), ">="),
        Pair(doubleArrayOf(2.0, -3.0), "<="),
        Pair(doubleArrayOf(4.0, 3.0), ">="),
        Pair(doubleArrayOf(-3.0, 1.0), "<=")
    ), doubleArrayOf(5.0, 7.0, 8.0, 8.0))

    val z = doubleArrayOf(3.0, 2.0)

    z.forEachIndexed{ id, _ ->
        freeVars["x${id+1}"] = id
        needVars.add("x${id+1}")
    }

    var (table, _) = createSimplexTable(z, a, b)


    println("Начальная симплекс-таблица")
    printData(table, basisVars, freeVars)


    var bArr = b.clone()

    while(needToDoPreStep(bArr)){
        val row = bArr.toList().indexOf(bArr.filter { it < 0 }.maxBy { abs(it) })
        val col = a[row].first.toList().indexOf(a[row].first.filter { it < 0 }.maxBy { abs(it) })

        println("$row $col")

        replaceVars(col, row)
        println(row to col)
        table = makePreStep(table, (row to col))

        bArr = table.slice(0..<table.size-1).map { it.last() }.toDoubleArray()
        println(bArr.joinToString(" "))

        println()
        println("Таблица после симплекс-шага")
        printData(table, basisVars, freeVars)
        println()
    }

    table = countDeltas(table, z)

    println("deltas")
    printData(table, basisVars, freeVars)


    while(needToDoStep(table.last(), "min")){
        val col = chooseColumn(table, "min")
        val row = chooseRow(table, bArr, col)

        replaceVars(col, row)
        println(row to col)
        table = makeSimplexStep(table, (row to col))
        bArr = table.slice(0..<table.size-1).map { it.last() }.toDoubleArray()

        println()
        println("Таблица после симплекс-шага")
        printData(table, basisVars, freeVars)
    }


    println()
    println("Решённая задача без условия целочисленности:")
    printData(table, basisVars, freeVars)

    println()
    var funVal = Z(z, needVars.map {
        if(basisVars[it] == null)
            0.0
        else
            table[basisVars[it]!!].last()
    }.toDoubleArray())

    z.forEachIndexed {id, it ->
        print("${it.toInt()}x${id+1}${if(id==z.lastIndex) " = " else " + "}")
    }
    println(funVal)

    z.forEachIndexed {id, it ->
        print("${it.toInt()} * ${if(basisVars["x${id+1}"] == null) 0.0 else table[basisVars["x${id+1}"]!!].last()}${if(id==z.lastIndex) " = " else " + "}")
    }
    println(funVal)


    while(!isAllBIntegers(table)){
        table = createNewConstraint(table)
        println()
        println("Таблица с новыми ограничениями: ")
        printData(table, basisVars, freeVars)
        val row = table.size-2
        val col = chooseColumnAfterRow(table, row)

        replaceVars(col, row)

        table = makeSimplexStep(table, (row to col))
        println()
        println("Таблица после сиплекс-шага")
        printData(table, basisVars, freeVars)
    }

    for(i in table.indices){
        for(j in table[0].indices){
            table[i][j] = table[i][j].toBigDecimal().setScale(0, RoundingMode.HALF_UP).toDouble()
        }
    }

    println()
    println("Итоговая таблица")
    printData(table, basisVars, freeVars)
    println()
    val answer = mutableMapOf<String, Int>()

    needVars.forEach{
        if(basisVars[it] == null) answer[it] = 0
        else answer[it] = table[basisVars[it]!!].last().toInt()
    }


    answer.keys.forEach {
        println("$it = ${answer[it]}")
    }

    println()
    funVal = Z(z, needVars.map {if(basisVars[it] == null)0.0 else table[basisVars[it]!!].last()}.toDoubleArray())

    z.forEachIndexed {id, it ->
        print("${it.toInt()}x${id+1}${if(id==z.lastIndex) " = " else " + "}")
    }
    println(funVal)

    z.forEachIndexed {id, it ->
        print("${it.toInt()} * ${if(basisVars["x${id+1}"] == null) 0.0 else table[basisVars["x${id+1}"]!!].last()}${if(id==z.lastIndex) " = " else " + "}")
    }
    println(funVal)

    println(isInConstraints(a, answer, b))
}