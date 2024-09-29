package transportTaskLab

data class TableClass(
    private var froms: MutableList<String>,
    private var tos: MutableList<String>,
    private var tariffMatrix: MutableList<MutableList<Int>>
) {

    init {
        require(isFit(froms.size, tos.size, tariffMatrix))
    }

    fun printData(){
        val lengths = mutableListOf<Int>()
        val tosMaxLength = mutableMapOf<Int, Int>()
        tos.forEachIndexed {i, it ->
            lengths.add(it.length)
            tosMaxLength[i] = it.length
        }

        val fromsMaxLength = froms.maxBy { it.length }.length

        val length = lengths.fold(fromsMaxLength) { acc, len ->
            acc + (len + 2) }

        printRepeatedly("=", length = length)
        printRepeatedly(" ", length = fromsMaxLength+1, after = "|", newLine = false)

        for(to in tos){
            print("$to |")
        }

        println()
        printRepeatedly("=", length = length)

        for(i in 0..<froms.size){
            print("${froms[i]} ${" ".repeat(fromsMaxLength - froms[i].length)}|")

            for(j in tariffMatrix.indices){
                print("${tariffMatrix[j][i]} ${" ".repeat(tosMaxLength[j]!! - tariffMatrix[j][i].toString().length)}|")
            }
            println()
            printRepeatedly("=", length = length)
        }
    }

    private fun printRepeatedly(str:String, length:Int, newLine:Boolean = true,
                                before:String = "", after:String = ""){
        if(newLine) println("$before${str.repeat(length)}$after")
        else print("$before${str.repeat(length)}$after")
    }

    private fun isFit(m:Int, n:Int, matrix:MutableList<MutableList<Int>>):Boolean{
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