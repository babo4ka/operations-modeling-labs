package transportTaskLab

fun main(){

    val tc = TableClass(
        froms = mutableListOf("Омск", "Новосибирск", "Томск"),
        tos = mutableListOf("Н. Новгород", "Пермь", "Краснодар"),
        stocks = listOf(2000, 1700, 1600),
        needs = listOf(2000, 1000, 2300),
        tariffMatrix = listOf(
            listOf(4668, 4083, 5925),
            listOf(5081, 4097, 5902),
            listOf(5727, 4798, 6888),
        )
    )

    printData(tc)

    println()

    var pm = createInitialPlan(tc)
    for(m in pm){
        println(m.joinToString(" "))
    }

    val (u, v) = createPotentials(tc, pm)

    println()
    println(u.joinToString(" "))
    println(v.joinToString(" "))

    var (optimal, max) = isOptimal(tc, pm, u, v)

    println("first $optimal $max")

    while(!optimal){
        pm = improvePlan(pm, max)
        break
        val (u, v) = createPotentials(tc, pm)
        val (o, m) = isOptimal(tc, pm, u, v)
        optimal = o
        max = m
        println(optimal)
    }

    //val newPlan = improvePlan(tc, pm, u, v)

    for(m in pm){
        println(m.joinToString(" "))
    }
}