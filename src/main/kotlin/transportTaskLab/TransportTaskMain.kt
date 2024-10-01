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

    val pm = createInitialPlan(tc)
    printData(tc, true)

    val pr = improvePlan(tc, pm)

    println()
    println(pr.first.joinToString())
    println(pr.second.joinToString())



//
//    tc.printData()
//
//    println()
//    tc.createInitialPlan()
//
//    tc.printData(true)
//
//    tc.improvePlan()
//


}