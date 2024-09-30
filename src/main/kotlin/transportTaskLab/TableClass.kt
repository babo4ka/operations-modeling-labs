package transportTaskLab

import java.lang.Integer.min


data class TableClass(
    private val froms: MutableList<String>,
    private val tos: MutableList<String>,
    private val tariffMatrix: List<List<Int>>,
    private val stocks: List<Int>,
    private val needs: List<Int>
) {

    private val stockWord = "Запасы"
    private val needsWord = "Потребность"
    private val stocksAndNeedsSum by lazy { stocks.sum() }

    val planMatrix: MutableList<MutableList<Int>> by lazy{
        MutableList(froms.size){MutableList(tos.size){0} }
    }

    init {
        require(isFit(froms.size, tos.size, tariffMatrix))
        require(stocks.sum() == needs.sum())
    }

    fun printData(){
        val lengths = mutableListOf<Int>()
        val tosMaxLength = mutableMapOf<Int, Int>()
        (tos + mutableListOf(stockWord)).forEachIndexed { i, it ->
            lengths.add(it.length)
            tosMaxLength[i] = it.length
        }

        val fromsMaxLength = (froms + mutableListOf(needsWord)).maxBy { it.length }.length

        val length = lengths.fold(fromsMaxLength) { acc, len ->
            acc + (len + 2) }

        printRepeatedly("=", length = length)
        printRepeatedly(" ", length = fromsMaxLength+1, after = "|", newLine = false)

        for(to in tos){
            print("$to |")
        }
        print(stockWord)

        println()
        printRepeatedly("=", length = length)


        for(i in tariffMatrix.indices){
            print("${froms[i]} ${" ".repeat(fromsMaxLength - froms[i].length)}|")

            tariffMatrix[i].forEachIndexed{ id, it ->
                print("$it ${" ".repeat(tosMaxLength[id]!! - it.toString().length)}|")
            }

            print("${stocks[i]} ${" ".repeat(tosMaxLength[tos.lastIndex+1]!! - stocks[i].toString().length)}")

            println()
            printRepeatedly("=", length = length)
        }

        print("$needsWord ${" ".repeat(fromsMaxLength - needsWord.length)}|")

        for(needsId in needs.indices){
            print("${needs[needsId]} ${" ".repeat(tosMaxLength[needsId]!! - needs[needsId].toString().length)}|")
        }
        print(stocksAndNeedsSum)
    }

    fun createInitialPlan(){
        val tempNeeds = needs.toMutableList()
        val tempStocks = stocks.toMutableList()
        var needsId = 0
        var stocksId = 0

        while(needsId < tempNeeds.size && stocksId < tempStocks.size){
            val minimalVal = min(tempNeeds[needsId], tempStocks[stocksId])

            tempNeeds[needsId] -= minimalVal
            tempStocks[stocksId] -= minimalVal

            planMatrix[needsId][stocksId] = minimalVal

            if(tempNeeds[needsId] == 0) needsId++
            if(tempStocks[stocksId] == 0) stocksId++
        }

        planMatrix.forEach {
            println(it.joinToString())
        }

        println(tempNeeds.joinToString())
        println(tempStocks.joinToString())
    }

    fun printPlan(){
        val lengths = mutableListOf<Int>()
        val tosMaxLength = mutableMapOf<Int, Int>()
        (tos + mutableListOf(stockWord)).forEachIndexed { i, it ->
            lengths.add(it.length)
            tosMaxLength[i] = it.length
        }

        val fromsMaxLength = (froms + mutableListOf(needsWord)).maxBy { it.length }.length

        val length = lengths.fold(fromsMaxLength) { acc, len ->
            acc + (len + 2) }

        printRepeatedly("=", length = length)
        printRepeatedly(" ", length = fromsMaxLength+1, after = "|", newLine = false)

        for(to in tos){
            print("$to |")
        }
        print(stockWord)

        println()
        printRepeatedly("=", length = length)


        for(i in tariffMatrix.indices){
            print("${froms[i]} ${" ".repeat(fromsMaxLength - froms[i].length)}|")

            tariffMatrix[i].forEachIndexed{ id, it ->
                print("$it(${planMatrix[i][id]}) ${" ".repeat(tosMaxLength[id]!! - it.toString().length)}|")
            }

            print("${stocks[i]} ${" ".repeat(tosMaxLength[tos.lastIndex+1]!! - stocks[i].toString().length)}")

            println()
            printRepeatedly("=", length = length)
        }

        print("$needsWord ${" ".repeat(fromsMaxLength - needsWord.length)}|")

        for(needsId in needs.indices){
            print("${needs[needsId]} ${" ".repeat(tosMaxLength[needsId]!! - needs[needsId].toString().length)}|")
        }
        print(stocksAndNeedsSum)
    }

    private fun printRepeatedly(str:String, length:Int, newLine:Boolean = true,
                                before:String = "", after:String = ""){
        if(newLine) println("$before${str.repeat(length)}$after")
        else print("$before${str.repeat(length)}$after")
    }

    private fun isFit(m:Int, n:Int, matrix:List<List<Int>>):Boolean{
        var isFit = true
        if(m == matrix.size){
            for (lst in matrix){
                if(lst.size != n){
                    isFit = false
                    break
                }
            }
        }else isFit = false

        return isFit
    }

}