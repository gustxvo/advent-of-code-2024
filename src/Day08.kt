private sealed interface Strategy

private data object Signal : Strategy

private data object Resonance : Strategy

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

    fun calculateAntiNodes(current: Vec2, subsequent: Vec2, mapRange: MapRange) = buildSet<Vec2> {
        val deltaX = current.x - subsequent.x
        val deltaY = current.y - subsequent.y

        Vec2(current.x + deltaX, current.y + deltaY).takeIf { position -> position in mapRange }?.let(::add)
        Vec2(subsequent.x - deltaX, subsequent.y - deltaY).takeIf { position -> position in mapRange }?.let(::add)
    }

    fun calculateAntiNodesWithResonance(current: Vec2, subsequent: Vec2, mapRange: MapRange) = buildSet<Vec2> {
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

    fun calculateAntiNodes(positions: List<Vec2>, mapRange: MapRange, strategy: Strategy) = buildSet<Vec2> {
        positions.dropLast(1).forEachIndexed { index, current ->
            val subsequentPositions = positions.slice(index + 1..positions.lastIndex)
            subsequentPositions.forEach { subsequent ->
                when (strategy) {
                    Signal -> calculateAntiNodes(current, subsequent, mapRange)
                    Resonance -> calculateAntiNodesWithResonance(current, subsequent, mapRange)
                }.run(::addAll)
            }
        }
    }

    fun uniqueAntiNodesLocations(map: List<String>, strategy: Strategy): Int {
        val mapRange = MapRange(map)
        return frequencyLocationsMap(map).flatMap { (_, positions) ->
            calculateAntiNodes(positions, mapRange, strategy)
        }.distinct().count()
    }

    fun part1(input: List<String>): Int = uniqueAntiNodesLocations(input, strategy = Signal)

    fun part2(input: List<String>): Int = uniqueAntiNodesLocations(input, strategy = Resonance)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
