fun main() {
    fun part1(input: List<String>): Int {
        var depth = 0
        var position = 0

        for (line in input) {
            val split = line.split(" ")
            val command = split[0]
            val value = split[1].toInt()

            when (command) {
                "forward" -> position += value
                "down" -> depth += value
                "up" -> depth -= value
            }
        }

        return depth * position
    }

    fun part2(input: List<String>): Int {
        var depth = 0
        var position = 0
        var aim = 0

        for (line in input) {
            val split = line.split(" ")
            val command = split[0]
            val value = split[1].toInt()

            when (command) {
                "down" -> aim += value
                "up" -> aim -= value
                "forward" -> {
                    position += value
                    depth += aim * value
                }
            }
        }

        return depth * position
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
