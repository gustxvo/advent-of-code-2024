fun main() {

    fun CharSequence.asLong(): Long = toString().toLong()

    fun Long.hasEvenNumberOfDigits(): Boolean = "$this".length % 2 == 0

    fun Long.splitInHalf(): List<Long> = this.toString().let { stone ->
        return stone.chunked(stone.length / 2, CharSequence::asLong)
    }

    fun blinks(stoneCounts: Map<Long, Long>): Map<Long, Long> = buildMap {
        for ((stone, occurrences) in stoneCounts) {
            when {
                stone == 0L -> {
                    val count = this[1] ?: 0
                    this[1] = count + occurrences
                }

                stone.hasEvenNumberOfDigits() -> {
                    val (first, second) = stone.splitInHalf()
                    val count1 = this[first] ?: 0
                    this[first] = count1 + occurrences

                    val count2 = this[second] ?: 0
                    this[second] = count2 + occurrences
                }

                else -> {
                    val count = this[stone * 2024] ?: 0
                    this[stone * 2024] = count + occurrences
                }
            }
        }
    }

    fun part1(input: String, numberOfBlinks: Int = 25): Long {
        var stoneGroups = buildMap<Long, Long> {
            val stoneArrangements = input.split(" ").map(String::toLong).groupingBy { it }.eachCount()
            for ((stone, count) in stoneArrangements) {
                this[stone] = count.toLong()
            }
        }

        repeat(numberOfBlinks) {
            stoneGroups = blinks(stoneGroups)
        }

        return stoneGroups.values.sum()
    }

    fun part2(input: String): Long = part1(input, numberOfBlinks = 75)

    val input = readTextInput("Day11")
    part1(input).println()
    part2(input).println()
}
