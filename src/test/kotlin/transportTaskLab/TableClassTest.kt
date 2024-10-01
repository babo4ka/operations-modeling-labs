package transportTaskLab

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class TableClassTest{


    private val tc = TableClass(
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

    private val firstPotentialsShouldBe = Pair(intArrayOf(0, 14, 1000), intArrayOf(4668, 4083, 5888))

    @Test
    fun testPrintDataTime(){
        val time = measureTimeMillis {
            printData(tc)
        }

        assert(time < 500)
    }


    @Test
    fun testFirstPotentials(){
        val planMatrix = createInitialPlan(tc)

        val firstPotentials = improvePlan(tc, planMatrix)



        assertAll(
            { assertTrue(firstPotentialsShouldBe.first.mapIndexed{id, it -> it == firstPotentials.first[id]}.all{true})},
            { assertTrue(firstPotentialsShouldBe.second.mapIndexed{id, it -> it == firstPotentials.second[id]}.all{true}) })
    }
}