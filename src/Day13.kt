fun main() {
    data class Point(val x: Int, val y: Int)
    data class Fold(val horizontal: Boolean, val value: Int)

    fun parse(input: List<String>): List<CharArray> {
        val points = input
            .takeWhile { it.isNotBlank() }
            .map { it.split(",").map(String::toInt).let { (x, y) -> Point(x, y) } }

        val width = points.maxOf { it.x } + 1
        val height = points.maxOf { it.y } + 1

        val grid = List(height) { CharArray(width) { '.' } }

        for ((x, y) in points) {
            grid[y][x] = '#'
        }

        return grid
    }

    fun parseFolds(input: List<String>) = input
        .takeLastWhile { it.startsWith("fold") }
        .map {
            it
                .substringAfter("along ")
                .split("=")
                .let { (dir, value) -> Fold(dir == "x", value.toInt()) }
        }

    fun fold(grid: List<CharArray>, fold: Fold): List<CharArray> {
        val height = if (fold.horizontal) grid.size else grid.size / 2
        val width = if (fold.horizontal) grid[0].size / 2 else grid[0].size

        val folded = List(height) { CharArray(width) { '.' } }

        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == '.') continue
                if (fold.horizontal && x == fold.value) continue
                if (!fold.horizontal && y == fold.value) continue

                if (fold.horizontal && x > fold.value) {
                    folded[y][grid[y].lastIndex - x] = '#'
                } else if (!fold.horizontal && y > fold.value) {
                    folded[grid.lastIndex - y][x] = '#'
                } else {
                    folded[y][x] = '#'
                }
            }
        }

        return folded
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)
        val folds = parseFolds(input)

        val folded = fold(grid, folds.first())

        return folded.sumOf { it.count { c -> c == '#' } }
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)
        val folds = parseFolds(input)

        val folded = folds.fold(grid) { acc, fold -> fold(acc, fold) }

        println(folded.joinToString("\n") { it.joinToString("") })

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
