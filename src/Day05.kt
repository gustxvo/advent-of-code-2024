
private typealias OrderingRules = Map<Int, List<Int>>

fun main() {

    fun List<Int>.middleIndex() = size / 2

    fun followsOrderingRules(pages: List<Int>, orderingRules: OrderingRules): Boolean {
        return pages.dropLast(1).mapIndexed { index, page ->
            val pagesAfter = pages.subList(index + 1, pages.size)
            val pagesAfterRule = orderingRules[page] ?: return false
            pagesAfterRule.containsAll(pagesAfter)
        }.all { it }
    }

    fun getOrderingRules(input: List<String>): OrderingRules {
        return input.groupBy { line ->
            line.substringBefore("|").toInt()
        }.mapValues { (_, pagesAfter) ->
            pagesAfter.map { page -> page.substringAfter("|").toInt() }
        }
    }

    fun List<Int>.reorderPages(orderingRules: OrderingRules): List<Int> {
        val pages = this.toMutableList()
        var index = 0
        while (true) {
            if (index == pages.size) index = 0
            val page = pages[index]
            val pagesAfterRule = orderingRules[page] ?: emptyList()
            val pagesAfter = pages.subList(index + 1, pages.size)
            if (pagesAfterRule.containsAll(pagesAfter)) {
                index++
                continue
            }
            pages[index] = pages[index + 1]
            pages[index + 1] = page
            index++
            if (followsOrderingRules(pages, orderingRules)) return pages
        }
        return pages
    }

    fun part1(input: List<String>): Int {
        val orderingRules = input.takeWhile(String::isNotEmpty).let(::getOrderingRules)
        val pagesPerUpdate = input.takeLastWhile(String::isNotEmpty).map { it.split(",").map(String::toInt) }
        return pagesPerUpdate.filter { pages -> followsOrderingRules(pages, orderingRules) }
            .sumOf { pages -> pages[pages.middleIndex()] }
    }

    fun part2(input: List<String>): Int {
        val orderingRules = input.takeWhile(String::isNotEmpty).let(::getOrderingRules)
        val pagesPerUpdate = input.takeLastWhile(String::isNotEmpty).map { it.split(",").map(String::toInt) }
        return pagesPerUpdate.filterNot { pages -> followsOrderingRules(pages, orderingRules) }
            .sumOf { pages ->
                val reordered = pages.reorderPages(orderingRules)
                reordered[pages.middleIndex()]
            }
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

