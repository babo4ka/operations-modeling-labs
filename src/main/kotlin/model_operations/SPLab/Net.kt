package model_operations.SPLab

class Net(transes:Array<Event>, numOfEvs:Int){

    val eventsNum = numOfEvs
    val transitions = transes

    lateinit var iterations: MutableList<Array<Int>>

    fun printEvents(){
        println("События: ")
        transitions.forEach {
            println("${it.start} -> ${it.end} (${it.time})")
        }
        println()
    }


    fun findEnter():Int{
        val evs = (1..9).map { it }.toMutableList()

        transitions.forEach {
            if(evs.contains(it.end)) evs.remove(it.end)
        }

        return evs[0]
    }

    fun findExit():Int{
        val evs = (1..9).map{it}.toMutableList()

        transitions.forEach {
            if(evs.contains(it.start)) evs.remove(it.start)
        }

        return evs[0]
    }

    fun findEarlierTimes():Array<Pair<Int, Int>>{
        val t = Array(9){0}

        var updated: Boolean


        iterations = mutableListOf()

        do{
            updated = false
            transitions.forEach {
                val newVal = maxOf(t[it.end-1], t[it.start-1] + it.time)
                if(newVal != t[it.end-1]) updated = true
                t[it.end-1] = newVal
            }

            iterations.add(t.clone())

        }while (updated)

        println()
        println("Вычисление раннего времени на разных итерациях: ")
        printIterations()

        return t.mapIndexed { id, it -> id+1 to it }.toTypedArray()
    }


    private fun printIterations(){
        val firstColLen = eventsNum.toString().length

        val colLens = iterations.map {
            it.max().toString().length
        }


        print("${" ".repeat(firstColLen)}|")
        iterations.indices.forEachIndexed { id, it ->
            print("$it${" ".repeat(colLens[id] - it.toString().length)}|")
        }
        println()

        (1..eventsNum).forEachIndexed { id, it ->
            print("$it${" ".repeat(firstColLen - it.toString().length)}|")

            iterations.forEachIndexed { i, el ->
                print("${el[id]}${" ".repeat(colLens[i] - el[id].toString().length)}|")
            }
            println()
        }
    }

}