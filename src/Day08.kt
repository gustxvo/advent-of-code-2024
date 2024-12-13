fun main() {

    data class Vec2(val x: Int, val y: Int)

    data class MapRange(val xRange: IntRange, val yRange: IntRange) {

        operator fun contains(position: Vec2) = position.x in xRange && position.y in yRange
    }

    fun MapRange(input: List<String>): MapRange = MapRange(xRange = input[0].indices, yRange = input.indices)

    fun frequencyLocationsMap(input: List<String>): Map<Char, List<Vec2>> = buildMap {
        for (y in input.indices) {
            for ((x, char) in input[y].withIndex()) {
                if (!char.isLetterOrDigit()) continue
                val positions = getOrElse(char) { listOf<Vec2>() }
                put(char, positions + Vec2(x, y))
            }
        }
    }

    fun antiNodesLocationsInMap(
        map: List<String>,
        antiNodesLocationsForFrequency: (Vec2, Vec2) -> Set<Vec2>,
    ): Int = frequencyLocationsMap(map)
        .flatMap { (_, positions) ->
            val antiNodesPositions = buildSet<Vec2> {
                positions.dropLast(1).forEachIndexed { index, currentPosition ->
                    val subsequentFrequencyPositions = positions.slice(index + 1..positions.lastIndex)
                    subsequentFrequencyPositions.forEach { subsequentPosition ->
                        addAll(antiNodesLocationsForFrequency(currentPosition, subsequentPosition))
                    }
                }
            }
            antiNodesPositions
        }.distinct().count()

    fun part1(input: List<String>): Int = MapRange(input).let { mapRange ->
        antiNodesLocationsInMap(input) { (thisX, thisY), (nextX, nextY) ->
            val deltaX = thisX - nextX
            val deltaY = thisY - nextY
            val next = Vec2(thisX + deltaX, thisY + deltaY).takeIf { position -> position in mapRange }
            val before = Vec2(nextX - deltaX, nextY - deltaY).takeIf { position -> position in mapRange }
            setOfNotNull(next, before)
        }
    }

    fun part2(input: List<String>): Int = MapRange(input).let { mapRange ->
        antiNodesLocationsInMap(input) { position, subsequentPosition ->
            val frequencyPositions = mutableSetOf(position)
            val deltaX = position.x - subsequentPosition.x
            val deltaY = position.y - subsequentPosition.y
            var forward = position
            var reverse = position
            do {
                forward = forward.copy(forward.x + deltaX, forward.y + deltaY)
                reverse = reverse.copy(reverse.x - deltaX, reverse.y - deltaY)
                if (forward in mapRange) frequencyPositions.add(forward)
                if (reverse in mapRange) frequencyPositions.add(reverse)
            } while (forward in mapRange || reverse in mapRange)
            frequencyPositions
        }
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
