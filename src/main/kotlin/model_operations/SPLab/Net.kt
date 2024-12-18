package model_operations.SPLab

class Net(transes:Array<Event>, numOfEvs:Int){

    private val eventsNum = numOfEvs
    private val transitions = transes
    private var criticalTime = -1

    private var earliestTimes:Array<Pair<Int, Int>>? = null
    private var latestTimes:Array<Pair<Int, Int>>? = null

    private lateinit var iterations: MutableList<Array<Int>>

    fun printEvents(){
        println("События: ")
        transitions.forEach {
            println("${it.start} -> ${it.end} (${it.time})")
        }
        println()
    }


    fun findEnter():Int{
        val evs = (1..eventsNum).map { it }.toMutableList()

        transitions.forEach {
            if(evs.contains(it.end)) evs.remove(it.end)
        }

        return evs[0]
    }

    fun findExit():Int{
        val evs = (1..eventsNum).map{it}.toMutableList()

        transitions.forEach {
            if(evs.contains(it.start)) evs.remove(it.start)
        }

        return evs[0]
    }

    fun findEarliestTimes():Array<Pair<Int, Int>>{
        val t = Array(eventsNum){0}

        var updated: Boolean


        iterations = mutableListOf()

        iterations.add(t.clone())

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

        criticalTime = t.last()

        earliestTimes = t.mapIndexed { id, it -> id+1 to it }.toTypedArray()
        return earliestTimes as Array<Pair<Int, Int>>
    }


    fun findLatestTimes():Array<Pair<Int, Int>>{
        val t = Array(eventsNum){criticalTime}

        var updated:Boolean

        iterations = mutableListOf()

        iterations.add(t.clone())
        do{
            updated = false
            transitions.forEach {
                val newVal = minOf(t[it.start-1], t[it.end-1] - it.time)
                if(newVal != t[it.start-1]) updated = true
                t[it.start-1] = newVal
            }

            iterations.add(t.clone())
        }while (updated)


        println()
        println("Вычисление позднего времени на разных итерациях: ")
        printIterations()

        latestTimes = t.mapIndexed { id, it -> id+1 to it }.toTypedArray()

        return latestTimes as Array<Pair<Int, Int>>
    }


    fun findReserves():Array<Pair<Int, Int>>{
        val reserves = mutableListOf<Pair<Int, Int>>()

        transitions.forEachIndexed { id, it ->
            reserves.add(
                id+1 to (latestTimes!!.first { el -> el.first == it.end }.second - earliestTimes!!.first{ el -> el.first == it.start}.second - it.time)
            )
        }

        return reserves.toTypedArray()
    }


    fun getCriticalWay():Array<Int>{
        val evs = mutableListOf<Int>()

        (1..eventsNum).forEach {
            if(latestTimes!!.first { el -> el.first == it }.second == earliestTimes!!.first { el -> el.first == it }.second)
                evs.add(it)
        }

        return evs.toTypedArray()
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