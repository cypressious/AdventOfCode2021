fun main() {
    val braces = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    val wrongScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    val autocompleteScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    fun getWrongSymbol(line: String): Char? {
        val stack = mutableListOf<Char>()

        for (c in line.toCharArray()) {
            when (c) {
                in braces -> stack += braces[c]!!
                stack.last() -> stack.removeLast()
                else -> return c
            }
        }

        return null
    }

    fun part1(input: List<String>): Int {
        return input.mapNotNull(::getWrongSymbol).map(wrongScores::getValue).sum()
    }

    fun getIncompleteStack(line: String): List<Char>? {
        val stack = mutableListOf<Char>()

        for (c in line.toCharArray()) {
            when (c) {
                in braces -> stack += braces[c]!!
                stack.last() -> stack.removeLast()
                else -> return null
            }
        }

        return stack
    }

    fun part2(input: List<String>): Long {
        return input
            .mapNotNull { getIncompleteStack(it) }
            .map { it.reversed().fold(0L) { acc, c -> acc * 5 + autocompleteScores.getValue(c) } }
            .sorted().let { it[it.size / 2] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
