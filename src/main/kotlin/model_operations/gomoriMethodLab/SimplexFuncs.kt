package model_operations.gomoriMethodLab


fun createSimplexTable(z:DoubleArray, a:Array<Pair<DoubleArray, String>>, b:DoubleArray):Array<DoubleArray>{

    val m = a.size // количество ограничений
    val n = z.size // количество переменных

    val colsCount = a.filter { it.second == ">=" }.size

    val table = Array(m+1) {DoubleArray(n + colsCount + 1)}

    var artIndex = 0

    for(i in 0..<m){
        for(j in 0..<n){
            table[i][j] = a[i].first[j]
        }

        table[i][table[i].lastIndex] = b[i]

        if(a[i].second == ">="){
            table[i][n+artIndex] = -1.0
            artIndex++
        }
    }

    for(i in 0..<z.size){
        table[m][i] = -z[i]
    }

    return table
}

fun main(){

    val a = arrayOf(
        Pair(doubleArrayOf(0.0, 2.0), ">="),
        Pair(doubleArrayOf(2.0, -3.0), "<="),
        Pair(doubleArrayOf(4.0, 3.0), ">="),
        Pair(doubleArrayOf(-3.0, 1.0), "<=")
    )

    val table = createSimplexTable(doubleArrayOf(3.0, 2.0), a, doubleArrayOf(5.0, 7.0, 8.0, 8.0))

    table.forEach {
        println(it.joinToString(" "))
    }
}