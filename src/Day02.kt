import kotlin.math.abs

fun main() {

    fun isReportValid(report: List<Int>): Boolean {
        val ordered = report == report.sorted() || report == report.sortedDescending()

        val inRange = report.let { numbers ->
            numbers.zipWithNext { a, b -> abs(a - b) in (1..3) }.all { it }
        }
        return ordered && inRange
    }

    fun part1(input: List<String>) = input.map { line -> line.split(" ").map(String::toInt) }
        .count(::isReportValid)

    fun isReportValidWithBadLevel(report: List<Int>): Boolean {
        for (i in report.indices) {
            val rep = report.toMutableList().apply { removeAt(i) }
            if (isReportValid(rep)) return true
        }
        return false
    }

    fun part2(input: List<String>) = input.map { line -> line.split(" ").map(String::toInt) }
        .count { report -> isReportValid(report) || isReportValidWithBadLevel(report) }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
