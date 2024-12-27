package model_systems.game_theory

fun matrixConv(a:Array<IntArray>):Array<IntArray>?{

    fun findDominantRows():MutableList<Int>{
        val isRowDominant: (IntArray, IntArray) -> Boolean = { row1, row2 ->
            row1.zip(row2).all { (a, b) -> a >= b }
        }

        val domRows = mutableListOf<Int>()

        for(i in a.indices){
            var isDom = true
            for(j in a.indices){
                if(i != j  && !isRowDominant(a[i], a[j])){
                    isDom = false
                    break
                }
            }

            if(isDom){
                domRows.add(i)
                if(domRows.size == 2) break
            }
        }

        return domRows
    }

    val domr = findDominantRows()
    println(domr.joinToString(" "))
    return null
}


fun main(){
    val a = arrayOf(
        intArrayOf(2, 6, 4, 2),
        intArrayOf(7, 2, 3, 1),
        intArrayOf(5, 3, 7, 5)
    )

    matrixConv(a)

}