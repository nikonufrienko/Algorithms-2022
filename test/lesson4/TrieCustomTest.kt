package lesson4

import util.generateString
import java.util.*
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class TrieCustomTest {

    private fun generateTestData(): List<SortedSet<String>> {
        val testData = mutableListOf<SortedSet<String>>()
        val random = Random()
        for (size in 1 until 100) {
            val values = mutableSetOf<String>()
            for (i in 0 until size) {
                values.add(generateString(abs(random.nextInt() % 100)))
            }
            testData.add(values.toSortedSet())
        }
        return testData
    }

    @Test
    fun testIterator() {
        val testData = generateTestData()

        testData.forEach {
            val trie = Trie()
            val values = it
            val result = mutableListOf<String>()
            values.forEach { value ->
                trie.add(value)
            }
            val iterator = trie.iterator()
            while (iterator.hasNext()) {
                val value = iterator.next()
                result.add(value)
            }
            assertEquals(values.toList(), result)
        }
    }

    @Test
    fun testRemoveIterator() {
        val testData = generateTestData()
        val random = Random()
        testData.forEach {
            val trie = Trie()
            val values = it.toMutableSet()
            val result = mutableListOf<String>()
            values.forEach { value ->
                trie.add(value)
            }
            val iterator = trie.iterator()
            while (iterator.hasNext()) {
                val value = iterator.next()
                if (random.nextBoolean()) {
                    iterator.remove()
                    values.remove(value)
                }
            }

            val iterator2 = trie.iterator()

            while (iterator2.hasNext()) {
                val value = iterator2.next()
                result.add(value)
            }
            assertEquals(values.sorted(), result)
        }
    }
}