package model_operations.transportTaskLab

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

    var it = 1

    printData(tc)
    println()
    println()

    println("Итерация $it:")
    var pm = createInitialPlan(tc)
    printPlan(pm)
    println()

    val (u, v) = createPotentials(tc, pm)
    printPotentials(tc, pm, u, v)
    println()
    println()

    var (optimal, max) = isOptimal(tc, pm, u, v)

    while(!optimal){
        it++

        println("Итерация $it:")
        pm = improvePlan(pm, max)
        printPlan(pm)
        println()

        val (u, v) = createPotentials(tc, pm)
        printPotentials(tc, pm, u, v)
        println()
        println()

        val (o, m) = isOptimal(tc, pm, u, v)
        optimal = o
        max = m
    }

    println("Итоговый план перевозок:")
    printPlan(pm)

    println("Итоговая цена: ${finalCost(pm, tc)}")
}