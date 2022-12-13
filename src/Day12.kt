import java.util.*

fun main() {
    class Node(
        val name: String,
        val edges: MutableSet<Node> = TreeSet(compareBy { it.name })
    ) {
        val isSmallCave get() = name.first().isLowerCase() && name != "start" && name != "end"
        val isBigCave get() = name.first().isUpperCase()

        override fun toString() = name

        fun getPathsTo(end: Node, visited: MutableMap<Node, Int>, allowSmallTwice: Boolean = false): Int {
            if (this == end) return 1

            if (!isBigCave) visited.compute(this) { _, v -> (v ?: 0) + 1 }

            val paths = edges
                .filter { it.canBeVisited(visited, allowSmallTwice) }
                .sumOf { it.getPathsTo(end, visited, allowSmallTwice) }

            if (!isBigCave) visited.compute(this) { _, v -> (v ?: 0) - 1 }

            return paths
        }

        private fun canBeVisited(visited: MutableMap<Node, Int>, allowSmallTwice: Boolean): Boolean {
            if (isBigCave) return true

            val visitCount = visited.getOrElse(this) { 0 }

            if (visitCount == 0) return true

            return allowSmallTwice && isSmallCave && visited.none { it.value == 2 }
        }
    }

    fun parse(input: List<String>): Pair<Node, Node> {
        val nodes = mutableMapOf<String, Node>()

        for (line in input) {
            val (from, to) = line.split("-").map { nodes.getOrPut(it) { Node(it) } }
            from.edges += to
            to.edges += from
        }

        return nodes.getValue("start") to nodes.getValue("end")
    }

    fun part1(input: List<String>): Int {
        val (start, end) = parse(input)

        return start.getPathsTo(end, mutableMapOf())
    }

    fun part2(input: List<String>): Int {
        val (start, end) = parse(input)

        return start.getPathsTo(end, mutableMapOf(), true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val testInput2 = readInput("Day12_test2")
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
