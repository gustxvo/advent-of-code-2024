fun main() {

    data class Vec2(val x: Int, val y: Int)

    data class MapRange(val xRange: IntRange, val yRange: IntRange) {

        operator fun contains(position: Vec2) = position.x in xRange && position.y in yRange
    }

    fun frequencyLocationsMap(input: List<String>): Map<Char, List<Vec2>> = buildMap {
        for (y in input.indices) {
            for ((x, char) in input[y].withIndex()) {
                if (!char.isLetterOrDigit()) continue
                val positions = getOrElse(char) { listOf<Vec2>() }
                put(char, positions + Vec2(x, y))
            }
        }
    }

    fun calculateAntiNodes(positions: List<Vec2>, mapRange: MapRange) = buildSet<Vec2> {
        positions.dropLast(1).forEachIndexed { index, (thisX, thisY) ->
            for (i in index + 1..positions.lastIndex) {
                val (nextX, nextY) = positions[i]
                val deltaX = thisX - nextX
                val deltaY = thisY - nextY
                Vec2(thisX + deltaX, thisY + deltaY).takeIf { position -> position in mapRange }?.let(::add)
                Vec2(nextX - deltaX, nextY - deltaY).takeIf { position -> position in mapRange }?.let(::add)
            }
        }
    }

    fun antiNodesResonance(positions: List<Vec2>, mapRange: MapRange) = buildSet<Vec2> {
        positions.dropLast(1).forEachIndexed { index, position ->
            for (i in index + 1..positions.lastIndex) {
                add(position)
                val (nextX, nextY) = positions[i]
                val deltaX = position.x - nextX
                val deltaY = position.y - nextY
                var forward = position
                var reverse = position
                do {
                   forward = forward.copy(forward.x + deltaX, forward.y + deltaY)
                   reverse = reverse.copy(reverse.x - deltaX, reverse.y - deltaY)
                   if (forward in mapRange) add(forward)
                   if (reverse in mapRange) add(reverse)
                } while (forward in mapRange || reverse in mapRange)
            }
        }
    }

    fun part1(input: List<String>): Int = frequencyLocationsMap(input)
        .flatMap { (_, positions) ->
            calculateAntiNodes(positions, MapRange(yRange = input.indices, xRange = input[0].indices))
        }
        .distinct()
        .count()

    fun part2(input: List<String>): Int = frequencyLocationsMap(input)
        .flatMap { (_, positions) ->
            antiNodesResonance(positions, MapRange(yRange = input.indices, xRange = input[0].indices))
        }
        .distinct()
        .count()

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
