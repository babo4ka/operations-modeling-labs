package model_systems.game_theory

data class GameData(
    var p:DoubleArray,
    var q:DoubleArray,
    var i:Int,
    var j:Int
)

fun countFreqs(a:Array<IntArray>, step:Int, gd:GameData? = null): GameData{
    val p = mutableListOf<Double>()
    val q = mutableListOf<Double>()

    if(step == 0){
        p.add(1.0)
        for(i in 0..<a.size-1){
            p.add(0.0)
        }

        q.add(1.0)
        for(j in 0..<a[0].size-1){
            q.add(0.0)
        }

        return GameData(p.toDoubleArray(), q.toDoubleArray(), 0, 0)
    }else{
        var alpha = 0.0
        var newI = 0

        for(i in a.indices){
            var tempSum = 0.0

            for(j in a[0].indices){
                tempSum += a[i][j] * gd!!.q[j]
            }

            if(tempSum > alpha){
                alpha = tempSum
                newI = i
            }
        }

        var beta = Double.MAX_VALUE
        var newJ = 0

        for(j in a[0].indices){
            var tempSum = 0.0

            for(i in a.indices){
                tempSum += a[i][j] * gd!!.p[i]
            }

            if(tempSum < beta){
                beta = tempSum
                newJ = j
            }
        }


        val v = (alpha + beta) / 2
        println("v($step) = $v")


        for(i in a.indices){
            if(i == newI){
                p.add((step * gd!!.p[i] + 1) / (step + 1))
            }else{
                p.add((step * gd!!.p[i]) / (step + 1))
            }
        }


        for(j in a[0].indices){
            if(j == newJ){
                q.add((step * gd!!.q[j] + 1) / (step + 1))
            }else{
                q.add((step * gd!!.q[j]) / (step + 1))
            }
        }


        return GameData(p.toDoubleArray(), q.toDoubleArray(), newI, newJ)
    }


}

fun main(){

    val a = arrayOf(
        intArrayOf(2, 6, 4, 2),
        intArrayOf(7, 2, 3, 1),
        intArrayOf(5, 3, 7, 5)
    )


    val steps = 12

    var gd:GameData? = null

    for(i in 0..<steps){
        gd = countFreqs(a, i, gd)
    }

}