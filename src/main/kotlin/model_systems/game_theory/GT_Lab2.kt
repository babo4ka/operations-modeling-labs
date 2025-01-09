package model_systems.game_theory

fun matrixConv(a:Array<IntArray>):Array<IntArray>{

    var matrix = a.clone()

    fun removeDominantRows():Array<IntArray>{
        val isRowDominant: (IntArray, IntArray) -> Boolean = { row1, row2 ->
            row1.zip(row2).all { (a, b) -> a >= b }
        }

        val domRows = mutableListOf<Int>()

        for(i in matrix.indices){
            var isDom = false
            for(j in matrix.indices){
                if(i != j  && isRowDominant(matrix[i], matrix[j]) && !domRows.contains(i) && !domRows.contains(j)){
                    isDom = true
                    break
                }
            }

            if(isDom){
                domRows.add(i)
            }
        }


        return matrix.filterIndexed { i, _ ->  domRows.contains(i)}.toTypedArray()
    }


    fun removeDominantCols(): Array<IntArray> {
        val isColumnDominant: (Int, Int) -> Boolean = { col1, col2 ->
            matrix.map { it[col1] }.zip(matrix.map { it[col2] }).all { (a, b) -> a >= b }
        }

        val domCols = mutableListOf<Int>()
        val colsCount = matrix[0].size

        for (j in 0..<colsCount) {
            var isDominant = false
            for (k in 0..<colsCount) {
                if (j != k && isColumnDominant(j, k) && !domCols.contains(k) && !domCols.contains(j)) {
                    isDominant = true
                    break
                }
            }
            if (isDominant) {
                domCols.add(j)
                if(domCols.size == 2) break
            }
        }

        val newArray = mutableListOf<IntArray>()

        matrix.forEach {
            newArray.add(it.filterIndexed { i, _ -> !domCols.contains(i) }.toIntArray())
        }

        return newArray.toTypedArray()
    }

    matrix = removeDominantCols()
    matrix = removeDominantRows()


    return matrix
}

val castToDouble: (Array<IntArray>) -> Array<DoubleArray> = {a -> a.map { it.map{ el -> el.toDouble()}.toDoubleArray() }.toTypedArray() }

fun transpose(a:Array<IntArray>):Array<IntArray>{
    val rows = a.size
    val cols = a[0].size

    val transposed = Array(cols){IntArray(rows) }


    for(i in 0..<rows){
        for(j in 0..<cols){
            transposed[j][i] = a[i][j]
        }
    }

    return transposed
}

val getPrice: (Array<IntArray>) -> Double = {a ->
    val arr = castToDouble(a)
    (arr[1][1] * arr[0][0] - arr[0][1] * arr[1][0]) / (arr[0][0] - arr[0][1] - arr[1][0] + arr[1][1])
}


val getFirstPlayerSolution: (Array<IntArray>) -> Pair<Double, Double> = { a ->
    val arr = castToDouble(a)

    val p1 = (arr[1][1] - arr[1][0]) / (arr[0][0] - arr[0][1] - arr[1][0] + arr[1][1])
    val p2 = (arr[0][0] - arr[0][1]) / (arr[0][0] - arr[0][1] - arr[1][0] + arr[1][1])

    p1 to p2
}


val getSecondPlayerSolution: (Array<IntArray>) -> Pair<Double, Double> = { a ->
    val transposed = transpose(a)
    val arr = castToDouble(transposed)

    val q1 = (arr[1][1] - arr[1][0]) / (arr[0][0] - arr[0][1] - arr[1][0] + arr[1][1])
    val q2 = (arr[0][0] - arr[0][1]) / (arr[0][0] - arr[0][1] - arr[1][0] + arr[1][1])

    q1 to q2
}

fun main(){
    val a = arrayOf(
        intArrayOf(2, 6, 4, 2),
        intArrayOf(7, 2, 3, 1),
        intArrayOf(5, 3, 7, 5)
    )

    println("Изначальная матрица:")
    a.forEach {
        println(it.joinToString(" "))
    }


    val mat = matrixConv(a)

    println("Упрощённая матрица:")
    mat.forEach {
        println(it.joinToString(" "))
    }


    val price = getPrice(mat)
    println("Цена игры: $price")


    val (p1, p2) = getFirstPlayerSolution(mat)
    val (q1, q2) = getSecondPlayerSolution(mat)

    println("Оптимальные стратегии первого игрока: |${"%.2f".format(p1)}; ${"%.2f".format(p2)}|")
    println("Оптимальные стратегии второго игрока: |$q1; $q2|")
}