package lesson1

import java.io.FileReader
import java.io.FileWriter
import java.io.Reader
import java.util.Random
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CustomTaskTests {
    fun getExecutionTime(function: () -> Unit): Long {
        val start = System.nanoTime()
        function()
        return System.nanoTime() - start
    }

    fun performanceTest(functionCall: (Int) -> Long, start: Int, end: Int, step: Int) {
        for (n in start until end step step) {
            val value = functionCall(n)
            println("n: $n executionTime: ${functionCall(n)}")
        }
    }

    //-273.0 до +500.0. //
    fun generateSequence(n: Int, outputFileName: String) {
        val random = Random()
        FileWriter(outputFileName).use {
            for (i in 0 until n) {
                val value = random.nextInt(7730) - 2730
                it.write("${value / 10}.${value % 10}\n")
            }
        }
    }

    @Test
    fun testSortSequence() {
        assertFails {
            performanceTest(
                { n ->
                    generateSequence(n, "input/input.txt")
                    getExecutionTime {
                        sortSequence("input/input.txt", "input/output.txt")
                    }
                }, 1, 1000_000, 100_000
            )
            sortSequence("input/input.txt", "input/output.txt")

        }
    }
}