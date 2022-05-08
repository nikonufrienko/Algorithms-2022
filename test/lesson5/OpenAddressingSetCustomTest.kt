package lesson5

import util.generateString
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenAddressingSetCustomTest {


    private fun generateControlSet(size: Int): MutableSet<String> {
        val controlSet = mutableSetOf<String>()
        for (j in 0 until size) {
            val value = generateString(24)
            controlSet.add(value)
        }
        return controlSet
    }

    @Test
    fun removeTest() {
        for (i in 0 until 10) {
            val setForTest = OpenAddressingSet<String>(6)
            val controlSet = generateControlSet(63)

            controlSet.forEach {
                setForTest.add(it)
            }

            controlSet.forEach {
                assertTrue(setForTest.contains(it))
            }

            val removed = mutableSetOf<String>()

            for (j in 0 until 5) {
                val value = controlSet.random()
                controlSet.remove(value)
                setForTest.remove(value)
                removed.add(value)
            }
            removed.forEach {
                assertFalse(setForTest.contains(it))
            }
        }
    }

    @Test
    fun iteratorTest() {
        val setForTest = OpenAddressingSet<String>(7)

        val controlSet = generateControlSet(127)
        controlSet.forEach {
            setForTest.add(it)
        }

        val iterator = setForTest.iterator()
        val resultControlSet = mutableSetOf<String>()
        while (iterator.hasNext()) {
            resultControlSet.add(iterator.next())
        }

        assertEquals(controlSet, resultControlSet)

        val removed = mutableSetOf<String>()

        for (j in 0 until 20) {
            val value = controlSet.random()
            controlSet.remove(value)
            removed.add(value)
        }

        val iterator2 = setForTest.iterator()

        while (iterator2.hasNext()) {
            if (removed.contains(iterator2.next()))
                iterator2.remove()
        }

        val resultControlSet2 = mutableSetOf<String>()

        val iterator3 = setForTest.iterator()

        while (iterator3.hasNext()) {
            resultControlSet2.add(iterator3.next())
        }
        assertEquals(controlSet, resultControlSet2)
    }
}