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

    fun antiNodeSignals(current: Vec2, subsequent: Vec2, mapRange: MapRange) = buildSet<Vec2> {
        val deltaX = current.x - subsequent.x
        val deltaY = current.y - subsequent.y

        Vec2(current.x + deltaX, current.y + deltaY).takeIf { position -> position in mapRange }?.let(::add)
        Vec2(subsequent.x - deltaX, subsequent.y - deltaY).takeIf { position -> position in mapRange }?.let(::add)
    }

    fun antiNodesWithResonance(current: Vec2, subsequent: Vec2, mapRange: MapRange) = buildSet<Vec2> {
        add(current)
        val deltaX = current.x - subsequent.x
        val deltaY = current.y - subsequent.y
        var forward = current
        var reverse = current
        do {
            forward = forward.copy(forward.x + deltaX, forward.y + deltaY)
            reverse = reverse.copy(reverse.x - deltaX, reverse.y - deltaY)
            if (forward in mapRange) add(forward)
            if (reverse in mapRange) add(reverse)
        } while (forward in mapRange || reverse in mapRange)
    }

    fun antiNodeLocations(
        positions: List<Vec2>,
        calculateAntiNodes: (Vec2, Vec2) -> Set<Vec2>,
    ): Set<Vec2> = buildSet<Vec2> {
        positions.dropLast(1).forEachIndexed { index, current ->
            val subsequentPositions = positions.slice(index + 1..positions.lastIndex)
            subsequentPositions.forEach { subsequent -> calculateAntiNodes(current, subsequent).run(::addAll) }
        }
    }

    fun uniqueAntiNodeLocations(map: List<String>, calculateAntiNodes: (Vec2, Vec2) -> Set<Vec2>): Int {
        return frequencyLocationsMap(map).flatMap { (_, positions) -> antiNodeLocations(positions, calculateAntiNodes) }
            .distinct()
            .count()
    }

    fun part1(input: List<String>): Int = MapRange(input).let { mapRange ->
        uniqueAntiNodeLocations(
            map = input,
            calculateAntiNodes = { current, subsequent -> antiNodeSignals(current, subsequent, mapRange) }
        )
    }

    fun part2(input: List<String>): Int = MapRange(input).let { mapRange ->
        uniqueAntiNodeLocations(
            map = input,
            calculateAntiNodes = { current, subsequent -> antiNodesWithResonance(current, subsequent, mapRange) }
        )
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
