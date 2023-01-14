fun main() {
    fun parseTarget(input: String) = input
            .split("=|,|(\\.\\.)".toRegex())
            .mapNotNull { it.toIntOrNull() }

    @Suppress("NAME_SHADOWING")
    fun simulate(
            target: List<Int>,
            dx: Int,
            dy: Int,
            xOnly: Boolean = false,
            yOnly: Boolean = false,
    ): Int {
        val (xMin, xMax, yMin, yMax) = target

        var x = 0
        var y = 0
        var dx = dx
        var dy = dy
        var wasRight = 0 > xMax
        var wasBelow = 0 < yMin
        var wasAbove = 0 > yMax
        var highestY = 0

        while ((yOnly || !(wasRight)) && (xOnly || !(wasAbove && wasBelow))) {
            if ((yOnly || x in xMin..xMax) && (xOnly || y in yMin..yMax)) {
                return highestY
            }

            x += dx
            y += dy
            highestY = maxOf(highestY, y)

            if (dx > 0) {
                dx--
            } else if (xOnly) {
                return -1
            }

            dy--

            if (x > xMax) wasRight = true
            if (y < yMin) wasBelow = true
            if (y > yMax) wasAbove = true
        }

        return -1
    }


    fun part1(input: String): Int {
        val target = parseTarget(input)

        return (1..1000).maxOf { simulate(target, 0, it, yOnly = true) }
    }

    fun part2(input: String): Int {
        val target = parseTarget(input)

        val minDx = (1..1000).first { simulate(target, it, 0, xOnly = true) >= 0 }
        val maxDx = (0..1000).last { simulate(target, it, 0, xOnly = true) >= 0 }
        val minDy = (0 downTo -1000).last { simulate(target, 0, it, yOnly = true) >= 0 }
        val maxDy = (0..1000).last { simulate(target, 0, it, yOnly = true) >= 0 }

        var count = 0

        for (dx in minDx..maxDx) {
            for (dy in minDy..maxDy) {
                if (simulate(target, dx, dy) >= 0) count++
            }
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = "target area: x=20..30, y=-10..-5"
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17").first()
    println(part1(input))
    println(part2(input))
}
