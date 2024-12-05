import kotlin.math.abs

fun main() {

    fun locationLists(input: List<String>): Pair<List<Int>, List<Int>> {
        return input.map { line ->
            val first = line.substringBefore(" ").toInt()
            val second = line.substringAfterLast(" ").toInt()

            first to second
        }.unzip()
    }

    fun part1(input: List<String>): Int {
        val (l1, l2) = locationLists(input)
        return l1.sorted().zip(l2.sorted())
            .sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        val (l1, l2) = locationLists(input)
        val frequencies = l2.groupingBy { it }.eachCount()

        return l1.sumOf { num -> num * frequencies.getOrDefault(num, 0) }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
