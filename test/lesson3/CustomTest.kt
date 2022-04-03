package lesson3

import kotlin.test.Test
import kotlin.test.assertEquals

class CustomTest {
    @Test
    fun testForIterator() {
        val tree = BinarySearchTree<Int>();
        val values = listOf(1, 123, 1, 233, 32, 1, 0, 2);
        values.forEach {
            tree.add(it);
        }
        val iterator = tree.iterator()
        val receivedValues = mutableListOf<Int>()
        while (iterator.hasNext()) {
            val value = iterator.next()
            receivedValues.add(value)
        }
        assertEquals(values.toSet().sorted(), receivedValues)

        val list = mutableListOf<Int>(1, 2, 3, 4, 5, 6, 7, 8)
        val toRemove = setOf<Int>(1, 5)

        val tree2 = BinarySearchTree<Int>();

        list.forEach {
            tree2.add(it);
        }

        val treeIterator = list.iterator()

        while (treeIterator.hasNext()) {
            val treeValue = treeIterator.next()
            println(treeValue)
            if (treeValue in toRemove) {
                treeIterator.remove();
            }
        }
        val treeIterator2 = list.iterator()

        while (treeIterator2.hasNext()) {
            val treeValue = treeIterator2.next()
            println(treeValue)
        }
        //не знаю что здесь не так
        assertEquals(6, tree2.size)

    }
}