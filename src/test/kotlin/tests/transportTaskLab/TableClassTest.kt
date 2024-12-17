package tests.transportTaskLab

import model_operations.transportTaskLab.*
import model_systems.game_theory.findSedlo
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


    private val initialTables = arrayOf(
        TableClass(
            froms = mutableListOf("Омск", "Новосибирск", "Томск"),
            tos = mutableListOf("Н. Новгород", "Пермь", "Краснодар"),
            stocks = listOf(2000, 1700, 1600),
            needs = listOf(2000, 1000, 2300),
            tariffMatrix = listOf(
                listOf(4668, 4083, 5925),
                listOf(5081, 4097, 5902),
                listOf(5727, 4798, 6888),
            )),

        TableClass(
                froms = mutableListOf("Омск", "Новосибирск", "Томск"),
                tos = mutableListOf("Н. Новгород", "Пермь", "Краснодар"),
                stocks = listOf(1300, 1200, 1100),
                needs = listOf(1000, 1500, 1100),
                tariffMatrix = listOf(
                    listOf(4668, 4083, 5925),
                    listOf(5081, 4097, 5902),
                    listOf(5727, 4798, 6888),)
            )
    )

    private val expectedTables = arrayOf(
        mutableListOf(
            mutableListOf(2000, -1, 0),
            mutableListOf(-1, -1, 1700),
            mutableListOf(-1, 1000, 600)),

        mutableListOf(
            mutableListOf(1000, 300, -1),
            mutableListOf(-1, 100, 1100),
            mutableListOf(-1, 1100, -1))
    )

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

        val firstPotentials = createPotentials(tc, planMatrix)



        assertAll(
            { assertTrue(firstPotentialsShouldBe.first.mapIndexed{id, it -> it == firstPotentials.first[id]}.all{true})},
            { assertTrue(firstPotentialsShouldBe.second.mapIndexed{id, it -> it == firstPotentials.second[id]}.all{true}) })
    }


    @Test
    fun testAnswer(){
        initialTables.forEachIndexed {id, it ->
            var pm = createInitialPlan(it)
            val(u, v) = createPotentials(it, pm)

            var (optimal, max) = isOptimal(it, pm, u, v)

            while(!optimal){
                pm = improvePlan(pm, max)
                val (u, v) = createPotentials(it, pm)
                val (o, m) = isOptimal(it, pm, u, v)
                optimal = o
                max = m
            }

            assertEquals(expectedTables[id], pm)
        }
    }


    @Test
    fun testSedlo1(){
        val a = arrayOf(
            intArrayOf(9, 3),
            intArrayOf(6, 4),
            intArrayOf(8, 7))
        val (found, _) = findSedlo(a)

        assertEquals(true, found)
    }
}