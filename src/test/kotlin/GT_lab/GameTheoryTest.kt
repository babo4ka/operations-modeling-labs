package GT_lab

import model_systems.game_theory.findSedlo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameTheoryTest {


    @Test
    fun testSedlo1(){
        val a = arrayOf(intArrayOf(3, 2), intArrayOf(1, 4))
        val (found, _) = findSedlo(a)

        assertEquals(true, found)
    }
}