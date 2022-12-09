fun main() {
    fun parse(input: List<String>) =
        input.map { it.toCharArray().map { c -> c.toString().toInt() } }

    fun isLowPoint(map: List<List<Int>>, y: Int, x: Int): Boolean {
        val value = map[y][x]

        if (y > 0 && map[y - 1][x] <= value) return false
        if (y < map.lastIndex && map[y + 1][x] <= value) return false
        if (x > 0 && map[y][x - 1] <= value) return false
        if (x < map[y].lastIndex && map[y][x + 1] <= value) return false

        return true
    }

    fun part1(input: List<String>): Int {
        val map = parse(input)

        var sum = 0

        for (y in map.indices) {
            for (x in map[y].indices) {
                if (isLowPoint(map, y, x)) sum += 1 + map[y][x]
            }
        }

        return sum
    }

    fun computeBasin(map: List<List<Int>>, lowY: Int, lowX: Int): MutableList<Pair<Int, Int>> {
        val toVisit = mutableListOf(lowY to lowX)
        val visited = mutableListOf<Pair<Int, Int>>()

        fun shouldAdd(pair: Pair<Int, Int>) = map[pair.first][pair.second] != 9 && pair !in visited && pair !in toVisit

        while (toVisit.isNotEmpty()) {
            val coordinate = toVisit.removeLast().also(visited::add)
            val (y, x) = coordinate

            (y - 1 to x).takeIf { it.first in map.indices && shouldAdd(it) }?.let(toVisit::add)
            (y + 1 to x).takeIf { it.first in map.indices && shouldAdd(it) }?.let(toVisit::add)
            (y to x - 1).takeIf { it.second in map[it.first].indices && shouldAdd(it) }?.let(toVisit::add)
            (y to x + 1).takeIf { it.second in map[it.first].indices && shouldAdd(it) }?.let(toVisit::add)
        }

        return visited
    }

    fun part2(input: List<String>): Int {
        val map = parse(input)
        val basins = mutableListOf<Int>()

        for (y in map.indices) {
            for (x in map[y].indices) {
                if (isLowPoint(map, y, x)) {
                    basins += computeBasin(map, y, x).size
                }
            }
        }

        basins.sortDescending()
        return basins.take(3).reduce(Int::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
