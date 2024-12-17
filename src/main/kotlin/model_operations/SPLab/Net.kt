package model_operations.SPLab

class Net(transes:Array<Event>, numOfEvs:Int){

    val eventsNum = numOfEvs
    val transitions = transes

    fun printEvents(){
        transitions.forEach {
            println("${it.start} -> ${it.end} (${it.time})")
        }
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

}