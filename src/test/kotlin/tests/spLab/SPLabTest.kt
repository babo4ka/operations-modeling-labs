package tests.spLab

import model_operations.SPLab.Event
import model_operations.SPLab.Net
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SPLabTest {
    val transitions = arrayOf(
        Event(1, 3, 24), Event(1, 4, 52),
        Event(4, 7, 12), Event(7, 9, 44),
        Event(2, 8, 26), Event(4, 5, 12),
        Event(5, 9, 64), Event(8, 9, 69),
        Event(2, 9, 17), Event(3, 5, 75),
        Event(3, 4, 3), Event(6, 7, 33),
        Event(2, 6, 50), Event(2, 4, 5),
        Event(2, 3, 0), Event(1, 6, 44),
        Event(4, 6, 29), Event(1, 2, 4)
    )

    val net = Net(transitions, 9)

    @Test
    fun testEnter(){
        val enter = net.findEnter()

        val earlier = net.findEarlierTimes()

        earlier.forEach {
            println("${it.first} --- ${it.second}")
        }

        assertEquals(1, enter)
    }
}