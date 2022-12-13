fun main() {
    fun parse(input: List<String>) =
        input.map { it.toCharArray().mapTo(mutableListOf()) { c -> c - '0' } }

    fun runCycle(grid: List<MutableList<Int>>): Int {
        var sum = 0
        val flashing = grid.map { BooleanArray(it.size) }

        fun flash(y: Int, x: Int) {
            sum++
            flashing[y][x] = true

            for (dy in -1..1) {
                for (dx in -1..1) {
                    if (dy == 0 && dx == 0) continue

                    val neighbor = grid.getOrNull(y + dy)?.getOrNull(x + dx) ?: continue
                    grid[y + dy][x + dx] = neighbor + 1

                    if (neighbor + 1 > 9 && !flashing[y + dy][x + dx]) flash(y + dy, x + dx)
                }
            }
        }

        for (y in grid.indices) {
            for (x in grid[y].indices) {
                grid[y][x]++
            }
        }

        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] > 9 && !flashing[y][x]) {
                    flash(y, x)
                }
            }
        }

        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (flashing[y][x]) {
                    grid[y][x] = 0
                }
            }
        }

        return sum
    }


    fun part1(input: List<String>): Int {
        val grid = parse(input)
        var sum = 0

        repeat(100) {
            sum += runCycle(grid)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)
        var count = 0

        while (grid.any { row -> row.any { it != 0 } }) {
            runCycle(grid)
            count++
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
