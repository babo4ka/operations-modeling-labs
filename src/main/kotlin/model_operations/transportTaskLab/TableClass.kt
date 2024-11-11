package model_operations.transportTaskLab

import kotlin.math.max
import kotlin.math.min


data class TableClass(
    val froms: MutableList<String>,
    val tos: MutableList<String>,
    val tariffMatrix: List<List<Int>>,
    val stocks: List<Int>,
    val needs: List<Int>
) {

//    private val stockWord = "Запасы"
//    private val needsWord = "Потребность"
      val stocksAndNeedsSum by lazy { stocks.sum() }
//
//
//
//    val planMatrix: MutableList<MutableList<Int>> by lazy{
//        MutableList(froms.size){MutableList(tos.size){-1} }
//    }
//
//    init {
//        require(isFit(froms.size, tos.size, tariffMatrix))
//        require(stocks.sum() == needs.sum())
//    }

//    fun printData(withPlan:Boolean = false){
//        val lengths = mutableListOf<Int>()
//        val tosMaxLength = mutableMapOf<Int, Int>()
//        (tos + mutableListOf(stockWord)).forEachIndexed { i, it ->
//            lengths.add(it.length)
//            tosMaxLength[i] = it.length
//        }
//
//        val fromsMaxLength = (froms + mutableListOf(needsWord)).maxBy { it.length }.length
//
//        val length = lengths.fold(fromsMaxLength) { acc, len ->
//            acc + (len + 2) }
//
//        printRepeatedly("=", length = length)
//        printRepeatedly(" ", length = fromsMaxLength+1, after = "|", newLine = false)
//
//        for(to in tos){
//            print("$to |")
//        }
//        print(stockWord)
//
//        println()
//        printRepeatedly("=", length = length)
//
//
//        for(i in tariffMatrix.indices){
//            print("${froms[i]} ${" ".repeat(fromsMaxLength - froms[i].length)}|")
//
//            tariffMatrix[i].forEachIndexed{ id, it ->
//                print("$it ${if(withPlan)"(" + planMatrix[i][id] + ")" else  " ".repeat(tosMaxLength[id]!! - it.toString().length)}|")
//            }
//
//            print("${stocks[i]} ${" ".repeat(tosMaxLength[tos.lastIndex+1]!! - stocks[i].toString().length)}")
//
//            println()
//            printRepeatedly("=", length = length)
//        }
//
//        print("$needsWord ${" ".repeat(fromsMaxLength - needsWord.length)}|")
//
//        for(needsId in needs.indices){
//            print("${needs[needsId]} ${" ".repeat(tosMaxLength[needsId]!! - needs[needsId].toString().length)}|")
//        }
//        print(stocksAndNeedsSum)
//    }
//
//    fun createInitialPlan(){
//        val tempNeeds = needs.toMutableList()
//        val tempStocks = stocks.toMutableList()
//        var needsId = 0
//        var stocksId = 0
//
//        while(needsId < tempNeeds.size && stocksId < tempStocks.size){
//            val minimalVal = minOf(tempNeeds[needsId], tempStocks[stocksId])
//
//            tempNeeds[needsId] -= minimalVal
//            tempStocks[stocksId] -= minimalVal
//
//            planMatrix[stocksId][needsId] = minimalVal
//
//            if(tempNeeds[needsId] == 0 && tempStocks[stocksId] == 0){
//                if(stocksId < tempStocks.size -1){
//                    planMatrix[needsId][stocksId+1] = 0
//                }else if(needsId < tempNeeds.size - 1){
//                    planMatrix[needsId+1][stocksId] = 0
//                }
//            }
//
//            if(tempNeeds[needsId] == 0) needsId++
//            if(tempStocks[stocksId] == 0) stocksId++
//        }
//    }
//
//    fun improvePlan() {
//        val m = planMatrix.size
//        val n = planMatrix[0].size
//
//        val u = IntArray(m){Int.MAX_VALUE}
//        val v = IntArray(n){Int.MAX_VALUE}
//
//        u[0] = 0
//
//        var changed = true
//        var a = 0
//        while (changed) {
//            changed = false
//
//            for (i in 0..<m) {
//                for (j in 0..<n) {
//                    if (planMatrix[i][j] >= 0) {
//                        if (u[i] != Int.MAX_VALUE && v[j] == Int.MAX_VALUE) {
//                            v[j] = tariffMatrix[i][j] - u[i]
//                            changed = true
//                        } else if (v[j] != Int.MAX_VALUE && u[i] == Int.MAX_VALUE) {
//                            u[i] = tariffMatrix[i][j] - v[j]
//                            changed = true
//                        }
//                    }
//                }
//            }
//            a++
//        }
//        println()
//        println(a)
//
//        println(u.joinToString())
//        println(v.joinToString())
//    }

//    private fun printRepeatedly(str:String, length:Int, newLine:Boolean = true,
//                                before:String = "", after:String = ""){
//        if(newLine) println("$before${str.repeat(length)}$after")
//        else print("$before${str.repeat(length)}$after")
//    }
//
//    private fun isFit(m:Int, n:Int, matrix:List<List<Int>>):Boolean{
//        var isFit = true
//        if(m == matrix.size){
//            for (lst in matrix){
//                if(lst.size != n){
//                    isFit = false
//                    break
//                }
//            }
//        }else isFit = false
//
//        return isFit
//    }

}