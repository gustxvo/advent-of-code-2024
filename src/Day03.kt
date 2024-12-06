fun main() {

    val multiplications = """mul\((\d+),(\d+)\)"""

    fun MatchResult.multiply(): Int {
        val (a, b) = destructured
        return a.toInt() * b.toInt()
    }

    fun part1(input: String): Int {
        return multiplications.toRegex().findAll(input)
            .sumOf(MatchResult::multiply)
    }

    fun part2(input: String): Int {
        val conditions = """don't\(\)|do\(\)"""
        val regex = """$multiplications|$conditions""".toRegex()
        var enabled = true
        var sum = 0

        regex.findAll(input).forEach { match ->
            when (match.value) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> if (enabled) sum += match.multiply()
            }
        }
        return sum
    }

    val input = readTextInput("Day03")
    part1(input).println()
    part2(input).println()
}
