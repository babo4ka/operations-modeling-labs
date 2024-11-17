package model_operations.gomoriMethodLab


fun createSimplexTable(z:DoubleArray, a:Array<Pair<DoubleArray, String>>, b:DoubleArray):Array<DoubleArray>{

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

    return table
}

fun chooseColumn(a:Array<DoubleArray>):Int{
    val zCol = a.last()
    return zCol.toList().indexOf(zCol.min())
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

    for(i in 0..<a[0].size){
        for(j in 0..<a.size){
            if(i == row || j == col) continue

            val mainDMul = a[i][j] * pivot
            val secDMul = a[i][col] * a[row][j]
            newTable[i][j] = (mainDMul - secDMul) / pivot
        }
    }

    return newTable
}


fun main(){

    val a = arrayOf(
        Pair(doubleArrayOf(0.0, 2.0), ">="),
        Pair(doubleArrayOf(2.0, -3.0), "<="),
        Pair(doubleArrayOf(4.0, 3.0), ">="),
        Pair(doubleArrayOf(-3.0, 1.0), "<=")
    )

    val b = doubleArrayOf(5.0, 7.0, 8.0, 8.0)

    val table = createSimplexTable(doubleArrayOf(3.0, 2.0), a, b)

    table.forEach {
        println(it.joinToString(" "))
    }

    val col = chooseColumn(table)
    val row = chooseRow(table, b, col)
    println()
    val newTable = makeSimplexStep(table, (row to col))

    newTable.forEach {
        println(it.joinToString(" "))
    }
}