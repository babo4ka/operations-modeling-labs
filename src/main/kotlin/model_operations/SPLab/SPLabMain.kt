package model_operations.SPLab

fun main(){

    val transitions = arrayOf(
        Event(1, 2, 11), Event(1, 3, 18),
        Event(1, 4, 6), Event(1, 6, 10),
        Event(2, 5, 11), Event(2, 6, 15),
        Event(3, 4, 14), Event(3, 6, 10),
        Event(4, 7, 19), Event(5, 8, 5),
        Event(6, 8, 11), Event(6, 9, 12),
        Event(7, 9, 16), Event(8, 9, 14))

    val net = Net(transitions, 9)

    net.printEvents()

    val enter = net.findEnter()
    val exit = net.findExit()


    println("Вход в сеть: $enter")
    println("Выход из сети: $exit")

    val earliest = net.findEarliestTimes()

    println()
    println("Ранее время для каждого события: ")
    earliest.forEach {
        println("${it.first} --- ${it.second}")
    }

    val latest = net.findLatestTimes()
    println()
    println("Позднее время для каждого события: ")
    latest.forEach {
        println("${it.first} --- ${it.second}")
    }

    val res = net.findReserves()

    println("Резервное время для каждого события: ")
    res.forEach {
        println("${it.first} --- ${it.second}")
    }


    println("Критические работы: ")
    res.forEach {
        if(it.second == 0){
            println(it.first)
        }
    }

    val criticalWay = net.getCriticalWay()
    println("Критическому пути принадлежат события: ")
    criticalWay.forEach {
        println(it)
    }
}