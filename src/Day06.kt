fun main() {

    fun part1(input: List<String>): Int {
        val fish = input[0].split(',').mapTo(mutableListOf(), String::toInt)

        repeat(80) {
            for (i in fish.indices) {
                fish[i]--

                if (fish[i] < 0) {
                    fish.add(8)
                    fish[i] = 6
                }
            }
        }

        return fish.size
    }

    fun part2(input: List<String>): Long {
        val fish = input[0].split(',').map(String::toInt).groupingBy { it }.eachCount()

        var counts = LongArray(9)

        for ((key, value) in fish) {
            counts[key] = value.toLong()
        }

        repeat(256) {
            val newCounts = LongArray(9)

            counts.drop(1).forEachIndexed { index, count -> newCounts[index] = count }
            newCounts[6] += counts[0]
            newCounts[8] = counts[0]

            counts = newCounts
        }

        return counts.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
