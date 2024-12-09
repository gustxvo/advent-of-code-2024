import Direction.*
private enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    fun turnRight(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }
}

fun main() {

    data class Position(val x: Int, val y: Int) {
        override fun toString(): String {
            return "Position(y=${y + 1}, x=${x + 2})"
        }
    }

    fun Position.inMapRange(map: List<String>) = y in map.indices && x in map.first().indices

    fun getStartingPoint(input: List<String>): Pair<Position, Direction> {
        val y = input.indexOfFirst { line -> line.contains("^") }
        val x = input[y].indexOf("^")
        val direction = when (input[y][x]) {
            '^' -> UP
            'v' -> DOWN
            '<' -> LEFT
            '>' -> RIGHT
            else -> error("No such direction")
        }
        return Position(x, y) to direction
    }

    fun getNextPosition(position: Position, direction: Direction): Position {
        val (x, y) = position
        return when (direction) {
            UP -> Position(x, y - 1)
            DOWN -> Position(x, y + 1)
            LEFT -> Position(x - 1, y)
            RIGHT -> Position(x + 1, y)
        }
    }

    fun getGuardPath(map: List<String>, startPos: Position, startDir: Direction) = buildSet<Position> {
        var position = startPos
        var direction = startDir
        while (true) {
            add(Position(position.x, position.y))
            val nextPosition = getNextPosition(position, direction)

            if (!nextPosition.inMapRange(map)) break
            val isObstacle = map[nextPosition.y][nextPosition.x] == '#'

            if (isObstacle) {
                direction = direction.turnRight()
            } else {
                position = nextPosition
            }
        }
    }


    fun isLoop(map: List<String>, startPos: Position, startDir: Direction, obstacle: Position): Boolean {
        var position = startPos
        var direction = startDir
        val visited = mutableListOf<Pair<Position, Direction>>()
        while (true) {
            visited.add(position to direction)
            val nextPosition = getNextPosition(position, direction)
            if (!nextPosition.inMapRange(map)) return false

            val hitNewObstacle = nextPosition == obstacle
            val isObstacle = map[nextPosition.y][nextPosition.x] == '#' || hitNewObstacle
            if (isObstacle) {
                direction = direction.turnRight()
            } else {
                position = nextPosition
            }
            val heading = position to direction
            if (visited.contains(heading)) return true
        }
        error("Unexpected")
    }

    fun part1(input: List<String>): Int {
        val (startPosition, startDirection) = getStartingPoint(input)
        val guardPath = getGuardPath(input, startPosition, startDirection)
        return guardPath.size
    }

    fun part2(input: List<String>): Int {
        var count = 0
        val (startPosition, startDirection) = getStartingPoint(input)
        for (y in input.indices) {
            for (x in input.first().indices) {
                val newObstacle = Position(x, y)
                if (isLoop(input, startPosition, startDirection, newObstacle)) count++
            }
        }
        return count
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
