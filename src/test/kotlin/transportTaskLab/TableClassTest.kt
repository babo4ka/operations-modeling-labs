package transportTaskLab

import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis


class TableClassTest{


    @Test
    fun testPrintDataTime(){
        val tc = TableClass(mutableListOf("Омск", "Новосибирск", "Томск", "Потребность"),
            mutableListOf("Н. Новгород", "Пермь", "Краснодар", "Запасы"),
            mutableListOf(
                mutableListOf(4668, 5081, 5727, 2000),
                mutableListOf(4083, 4097, 4798, 1000),
                mutableListOf(5925, 5902, 6888, 2300),
                mutableListOf(2000, 1700, 1600, 5300)
            )
        )

        val time = measureTimeMillis {
            tc.printData()
        }

        assert(time < 500)
    }
}