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

    val earlier = net.findEarlierTimes()

    println()
    println("Ранее время для каждого события: ")
    earlier.forEach {
        println("${it.first} --- ${it.second}")
    }
}