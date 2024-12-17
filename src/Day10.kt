private enum class TrailDirection(val x: Int, val y: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0)
}

fun main() {

    data class Vec2(val x: Int, val y: Int) {

        operator fun plus(direction: TrailDirection) = Vec2(x + direction.x, y + direction.y)
    }

    operator fun List<String>.contains(step: Vec2) = step.x in first().indices && step.y in this.indices

    fun List<String>.stepAt(pos: Vec2): Char = (this[pos.y][pos.x])

    fun Vec2.isSubsequentTo(previous: Vec2, map: List<String>) = map.stepAt(this) == map.stepAt(previous) + 1

    fun findTrailheadPositions(map: List<String>) = buildList<Vec2> {
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == '0') add(Vec2(x, y))
            }
        }
    }

    fun calculateTrails(
        map: List<String>,
        currentPath: List<Vec2>,
    ): List<List<Vec2>> {
        val current = currentPath.last()

        if (map.stepAt(current) == '9') return listOf(currentPath)

        val trails = TrailDirection.entries.filter { direction ->
            val next = current + direction
            next in map && next.isSubsequentTo(current, map)
        }.flatMap { direction ->
            val next = current + direction
            calculateTrails(map, currentPath + next)
        }

        return trails
    }

    fun calculateTrailScore(firstStep: Vec2, map: List<String>): Int {
        val hikingTrails = calculateTrails(
            map = map,
            currentPath = listOf(firstStep),
        )
        return hikingTrails.distinctBy { trail -> trail.last() }.count()
    }

    fun calculateTrailRating(firstStep: Vec2, map: List<String>): Int {
        val hikingTrails = calculateTrails(
            map = map,
            currentPath = listOf(firstStep),
        )
        return hikingTrails.count()
    }

    fun part1(input: List<String>): Int {
        val trailheadPositions = findTrailheadPositions(input)
        return trailheadPositions.sumOf { trailhead -> calculateTrailScore(trailhead, input) }
    }

    fun part2(input: List<String>): Int {
        val trailheadPositions = findTrailheadPositions(input)
        return trailheadPositions.sumOf { trailhead -> calculateTrailRating(trailhead, input) }
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

