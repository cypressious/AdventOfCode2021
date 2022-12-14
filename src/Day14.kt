fun main() {
    fun parseInsertions(input: List<String>) = input
        .drop(2)
        .associate { it.split(" -> ").let { (from, to) -> from to to[0] } }

    fun part1(input: List<String>): Int {
        var template = input.first().toList()
        val insertions = parseInsertions(input)

        repeat(10) {
            val newTemplate = mutableListOf<Char>()
            for (i in 0 until template.lastIndex) {
                newTemplate.add(template[i])
                val inserted = insertions.getValue("" + template[i] + template[i + 1])
                newTemplate.add(inserted)
            }
            newTemplate.add(template.last())
            template = newTemplate
        }

        val entries = template.groupingBy { it }.eachCount().values
        return entries.max() - entries.min()
    }

    fun part2(input: List<String>): Long {
        val line = input.first()
        var pairs = line.toList()
            .windowed(2, 1)
            .map { "" + it[0] + it[1] }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        val insertions = parseInsertions(input)

        repeat(40) {
            val newPairs = mutableMapOf<String, Long>()

            for ((pair, count) in pairs) {
                val char = insertions.getValue(pair)
                newPairs.merge("" + pair[0] + char, count, Long::plus)
                newPairs.merge("" + char + pair[1], count, Long::plus)
            }

            pairs = newPairs
        }

        val counts = mutableMapOf<Char, Long>()
        for ((pair, count) in pairs) {
            counts.merge(pair[0], count, Long::plus)
        }
        counts.merge(line.last(), 1, Long::plus)

        return counts.values.max() - counts.values.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
