package util

import org.jetbrains.research.runner.perf.PerfEstimation
import org.jetbrains.research.runner.perf.estimate
import java.util.*

data class PerfResult<T>(
    val size: Int = 0,
    val time: Long = 0,
    val data: T
)

fun <T> estimate(data: List<PerfResult<T>>) = estimate(data.map { it.size to it.time })

fun <T> estimate(sizes: List<Int>, body: (Int) -> PerfResult<T>): PerfEstimation {
    val data: MutableList<PerfResult<T>> = mutableListOf()

    for (size in sizes) {
        data += body(size)
    }

    return estimate(data)
}

private class StaticGenerator {
    companion object {
        val encoder = Base64.getEncoder()
        val random = Random()
    }
}


fun generateString(size: Int): String {
    val byteArray = ByteArray(size)
    StaticGenerator.random.nextBytes(byteArray)
    return StaticGenerator.encoder.encodeToString(byteArray)
}
