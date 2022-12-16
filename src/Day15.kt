import java.util.PriorityQueue

fun main() {
    val neighbors = listOf(
        -1 to 0,
        1 to 0,
        0 to -1,
        0 to 1,
    )

    data class Node(val x: Int, val y: Int, val risk: Int) : Comparable<Node> {
        var distance = Int.MAX_VALUE

        fun neighbors(grid: List<List<Node>>) = neighbors.mapNotNull { (dx, dy) ->
            grid.getOrNull(y + dy)?.getOrNull(x + dx)
        }

        override fun compareTo(other: Node) = distance.compareTo(other.distance)
    }

    fun findPath(input: List<String>, wrap: Boolean): Int {
        var grid = input.mapIndexed { y, row ->
            row.toCharArray().mapIndexed { x, c -> Node(x, y, c - '0') }
        }

        if (wrap) {
            val originalHeight = grid.size
            val originalWidth = grid[0].size

            grid = List(originalHeight * 5) { y ->
                val yy = y % originalHeight
                val dy = y / originalHeight

                List(originalWidth * 5) { x ->
                    val xx = x % originalWidth
                    val dx = x / originalWidth
                    Node(x, y, ((grid[yy][xx].risk + dx + dy - 1) % 9) + 1)
                }
            }
        }

        val unvisited = PriorityQueue<Node>()
        val visited = mutableSetOf<Node>()

        grid[0][0].let {
            it.distance = 0
            unvisited.add(it)
        }

        while (unvisited.isNotEmpty()) {
            val node = unvisited.poll()
            for (neighbor in node.neighbors(grid)) {
                if (neighbor !in visited && neighbor.distance > node.distance + neighbor.risk) {
                    unvisited.remove(neighbor)
                    neighbor.distance = node.distance + neighbor.risk
                    unvisited.add(neighbor)

                    if (neighbor.y == grid.lastIndex && neighbor.x == grid.last().lastIndex) {
                        return neighbor.distance
                    }
                }
            }
            visited += node
        }

        return 0
    }

    fun part1(input: List<String>): Int {
        return findPath(input, false)
    }

    fun part2(input: List<String>): Int {
        return findPath(input, true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
