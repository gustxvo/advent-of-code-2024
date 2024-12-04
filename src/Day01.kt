import kotlin.math.abs

fun main() {

    fun locationLists(input: List<String>): Pair<List<Int>, List<Int>> {
        val l1 = mutableListOf<Int>()
        val l2 = mutableListOf<Int>()
        input.forEach { line ->
            val a = line.substringBefore(" ").toInt()
            val b = line.substringAfterLast(" ").toInt()
            l1.add(a)
            l2.add(b)
        }
        return l1 to l2
    }

    fun part1(input: List<String>): Int {
        val (l1, l2) = locationLists(input)
        return l1.sorted().zip(l2.sorted())
            .sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        val (l1, l2) = locationLists(input)
        val elementCount = mutableMapOf<Int, Int>()
        l1.forEach {
            elementCount.put(it, l1.count { i -> i == it })
        }

        val elementScore = l1.associate { first ->
            first to l2.count { first == it }
        }

        return elementScore.entries.sumOf { (k, v) ->
            k * v * elementCount.getOrDefault(k, 0)
        }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
