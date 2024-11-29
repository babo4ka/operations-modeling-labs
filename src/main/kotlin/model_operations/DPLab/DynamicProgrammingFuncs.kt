package model_operations.DPLab


fun makeTable(m:Array<Int>, p:Int):MutableList<MutableList<Int>>{

    val table = MutableList(m.size+1){MutableList(p+1){0} }

    table[0].map { 0 }

    table.forEach { it[0] = 0 }

    return table
}


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
            }

            continue
        }
    }

    println(table[n][p])

    return table
}



fun main(){

    val m = intArrayOf(4, 5, 3, 7, 6)
    val c = intArrayOf(5, 7, 4, 9, 8)
    val p = 15


    val table = fillTable(m, c, p)

    table.forEach {
        println(it.joinToString(" "))
    }

}