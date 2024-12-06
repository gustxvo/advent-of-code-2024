private const val XMAS = "XMAS"

fun main() {

    fun part1(input: List<String>): Int {
        var appearances = 0
        for ((lineIdx, line) in input.withIndex()) {
            for ((charIdx, char) in line.withIndex()) {
                if (char != 'X') continue
                val leftBound = (charIdx - 3).takeIf { it > -1 }
                val rightBound = (charIdx + 3).takeIf { it < line.length }
                val upperBound = (lineIdx - 3).takeIf { it > -1 }
                val lowerBound = (lineIdx + 3).takeIf { it < input.size }
                val upperLeftBound = upperBound != null && leftBound != null
                val upperRightBound = upperBound != null && rightBound != null
                val lowerLeftBound = lowerBound != null && leftBound != null
                val lowerRightBound = lowerBound != null && rightBound != null
                leftBound?.run {
                    if (line.substring(this, charIdx + 1) == XMAS.reversed()) {
                        appearances++
                    }
                }
                rightBound?.run {
                    if (line.substring(charIdx, this + 1) == XMAS) {
                        appearances++
                    }
                }
                upperBound?.run {
                    val second = input[upperBound + 2][charIdx]
                    val third = input[upperBound + 1][charIdx]
                    val last = input[upperBound][charIdx]
                    val word = charArrayOf(char, second, third, last).concatToString()
                    if (word == XMAS) {
                        appearances++
                    }
                }
                lowerBound?.run {
                    val second = input[lowerBound - 2][charIdx]
                    val third = input[lowerBound - 1][charIdx]
                    val last = input[lowerBound][charIdx]
                    val word = charArrayOf(char, second, third, last).concatToString()
                    if (word == XMAS) {
                        appearances++
                    }
                }

                // Diagonals
                if (upperLeftBound) {
                    val second = input[upperBound + 2][leftBound + 2]
                    val third = input[upperBound + 1][leftBound + 1]
                    val last = input[upperBound][leftBound]
                    val word = charArrayOf(char, second, third, last).concatToString()
                    if (word == XMAS) {
                        appearances++
                    }
                }
                if (upperRightBound) {
                    val second = input[upperBound + 2][rightBound - 2]
                    val third = input[upperBound + 1][rightBound - 1]
                    val last = input[upperBound][rightBound]
                    val word = charArrayOf(char, second, third, last).concatToString()
                    if (word == XMAS) {
                        appearances++
                    }
                }
                if (lowerLeftBound) {
                    val second = input[lowerBound - 2][leftBound + 2]
                    val third = input[lowerBound - 1][leftBound + 1]
                    val last = input[lowerBound][leftBound]
                    val word = charArrayOf(char, second, third, last).concatToString()
                    if (word == XMAS) {
                        appearances++
                    }
                }
                if (lowerRightBound) {
                    val second = input[lowerBound - 2][rightBound - 2]
                    val third = input[lowerBound - 1][rightBound - 1]
                    val last = input[lowerBound][rightBound]
                    val word = charArrayOf(char, second, third, last).concatToString()
                    if (word == XMAS) {
                        appearances++
                    }
                }
            }
        }
        return appearances
    }

    fun part2(input: List<String>): Int {
        var appearances = 0
        val chars = listOf('M', 'S')
        for ((lineIdx, line) in input.withIndex()) {
            for ((charIdx, char) in line.withIndex()) {
                if (char != 'A') continue
                val leftBound = (charIdx - 1).takeIf { it > -1 } ?: continue
                val rightBound = (charIdx + 1).takeIf { it < line.length } ?: continue
                val upperBound = (lineIdx - 1).takeIf { it > -1 } ?: continue
                val lowerBound = (lineIdx + 1).takeIf { it < input.size } ?: continue

                val upperLeft = input[upperBound][leftBound]
                val upperRight = input[upperBound][rightBound]
                val lowerLeft = input[lowerBound][leftBound]
                val lowerRight = input[lowerBound][rightBound]
                val firstDiagonal = listOf(upperLeft, lowerRight)
                val secondDiagonal = listOf(upperRight, lowerLeft)
                val containsXMas = firstDiagonal.containsAll(chars) && secondDiagonal.containsAll(chars)

                if (containsXMas) {
                    appearances++
                }
            }
        }
        return appearances
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
