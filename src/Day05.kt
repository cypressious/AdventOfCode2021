fun main() {
    class Coordinate(val x: Int, val y: Int)

    fun List<Coordinate>.isHorizontal() = this[0].y == this[1].y
    fun List<Coordinate>.isVertical() = this[0].x == this[1].x

    fun sign(n: Int) = if (n > 0) 1 else -1

    fun handle(input: List<String>, countDiagonal: Boolean): Int {
        val lines = input
            .map { it.split(" -> ") }
            .map { line ->
                line.map {
                    val parts = it.split(',')
                    Coordinate(parts[0].toInt(), parts[1].toInt())
                }
            }

        var maxX = 0
        var maxY = 0

        for (line in lines) {
            for (coordinate in line) {
                maxX = maxOf(maxX, coordinate.x)
                maxY = maxOf(maxY, coordinate.y)
            }
        }

        val map = Array(maxX + 1) { IntArray(maxY + 1) }

        for (line in lines) {
            val first = line.first()
            val second = line.last()

            if (line.isHorizontal()) {
                for (x in minOf(first.x, second.x)..maxOf(first.x, second.x)) {
                    map[x][first.y]++
                }
            } else if (line.isVertical()) {
                for (y in minOf(first.y, second.y)..maxOf(first.y, second.y)) {
                    map[first.x][y]++
                }
            } else if (countDiagonal) {
                val xStep = sign(second.x - first.x)
                val yStep = sign(second.y - first.y)

                var x = first.x
                var y = first.y

                while (x in minOf(first.x, second.x)..maxOf(first.x, second.x)) {
                    map[x][y]++

                    x += xStep
                    y += yStep
                }
            }
        }

        return map.sumOf { line -> line.count { it >= 2 } }
    }

    fun part1(input: List<String>): Int {
        return handle(input, false)
    }

    fun part2(input: List<String>): Int {
        return handle(input, true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
