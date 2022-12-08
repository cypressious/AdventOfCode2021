import kotlin.math.abs

fun main() {
    fun parsePositions(input: List<String>): Map<Int, Int> {
        val positions = input[0].split(",").map(String::toInt)

        return positions.groupingBy { it }.eachCount()
    }

    fun part1(input: List<String>): Int {
        val countsByPosition = parsePositions(input)

        var cheapest = Int.MAX_VALUE

        for (target in countsByPosition.keys.min()..countsByPosition.keys.max()) {
            cheapest = minOf(
                countsByPosition.entries.sumOf { (pos, count) -> abs(target - pos) * count },
                cheapest
            )
        }

        return cheapest
    }

    fun sumBetweenOneAnd(n: Int) = (n * (n + 1)) / 2

    fun part2(input: List<String>): Int {
        val countsByPosition = parsePositions(input)

        var cheapest = Int.MAX_VALUE

        for (target in countsByPosition.keys.min()..countsByPosition.keys.max()) {
            cheapest = minOf(
                countsByPosition.entries.sumOf { (pos, count) -> sumBetweenOneAnd(abs(target - pos)) * count },
                cheapest
            )
        }

        return cheapest
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
