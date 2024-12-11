import kotlin.math.pow

private enum class Operation(val value: String) {
    ADDITION("+"),
    MULTIPLICATION("*"),
    CONCATENATION("||");

    override fun toString() = value
}

private typealias Combinations = Set<List<Operation>>

fun main() {

    infix fun Int.powerOf(other: Int) = (this.toFloat().pow(other)).toInt()

    fun operationCombinations1(numberOfOperations: Int) = buildSet<List<Operation>> {
        val combinations = 2 powerOf numberOfOperations

        for (i in 0..<combinations) {
            val combination = buildList<Operation> {
                for (j in 0..<numberOfOperations) {
                    val operationIdx = (i shr j) and 1
                    add(Operation.entries[operationIdx])
                }
            }
            add(combination)
        }
    }

    fun operationCombinations2(numberOfOperations: Int) = buildSet<List<Operation>> {
        val combinations = 3 powerOf numberOfOperations

        for (i in 0..<combinations) {
            val combination = buildList<Operation> {
                for (j in 0..<numberOfOperations) {
                    val operation = i.toString(radix = 3)
                    val operationIdx = operation.getOrNull(operation.lastIndex - j)?.digitToInt() ?: 0
                    add(Operation.entries[operationIdx])
                }
            }
            add(combination)
        }
    }

    fun calculateCombinations(
        constants: List<String>,
        includeConcat: Boolean = false,
    ): Map<Int, Combinations> = constants.groupBy { line ->
        val numberOfOperations = line.substringAfter(": ").split(" ").map(String::toInt).size - 1
        numberOfOperations
    }.mapValues { (numberOfOperations) ->
        if (includeConcat) operationCombinations2(numberOfOperations)
        else operationCombinations1(numberOfOperations)
    }

    fun testEquationValid(testValue: Long, constants: List<Long>, combinations: Map<Int, Combinations>): Boolean {
        val operationCombinations = combinations[constants.size - 1] ?: error("Unexpected number of combinations")
        return operationCombinations.any { operations ->
            var acc = constants.first()
            for (index in 1..constants.lastIndex) {
                when (operations[index - 1]) {
                    Operation.ADDITION -> acc += constants[index]
                    Operation.MULTIPLICATION -> acc *= constants[index]
                    else -> error("Concatenation not yet supported")
                }
            }
            testValue == acc
        }
    }

    fun testEquation2(testValue: Long, constants: List<Long>, combinations: Map<Int, Combinations>): Boolean {
        val operationCombinations = combinations[constants.size - 1] ?: error("Unexpected number of combinations")
        return operationCombinations.any { operations ->
            var acc = constants.first()
            for (index in 1..constants.lastIndex) {
                when (operations[index - 1]) {
                    Operation.ADDITION -> acc += constants[index]
                    Operation.MULTIPLICATION -> acc *= constants[index]
                    Operation.CONCATENATION -> acc = ("$acc${constants[index]}").toLong()
                }
            }
            testValue == acc
        }
    }

    fun part1(input: List<String>): Long {
        val combinations = calculateCombinations(input)
        return input.sumOf { line ->
            val testValue = line.substringBefore(":").toLong()
            val constants = line.substringAfter(": ").split(" ").map(String::toLong)
            if (testEquationValid(testValue, constants, combinations)) testValue else 0
        }
    }

    fun part2(input: List<String>): Long {
        val combinations = calculateCombinations(input, includeConcat = true)
        return input.sumOf { line ->
            val testValue = line.substringBefore(":").toLong()
            val constants = line.substringAfter(": ").split(" ").map(String::toLong)
            val isEquationValid = testEquation2(testValue, constants, combinations)

            if (isEquationValid) testValue else 0
        }
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
