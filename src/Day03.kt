fun main() {
    fun bitSums(input: List<String>): IntArray {
        val sums = IntArray(input[0].length)

        for (s in input) {
            for ((index, c) in s.withIndex()) {
                if (c == '1') {
                    sums[index]++
                }
            }
        }

        return sums
    }

    fun part1(input: List<String>): Int {
        val sums = bitSums(input)

        var gamma = 0
        var epsilon = 0

        val bitCount = input[0].length
        val count = input.size

        for (index in sums.indices) {
            if (sums[index] > count / 2) {
                gamma = gamma or (1 shl bitCount - index - 1)
            } else {
                epsilon = epsilon or (1 shl bitCount - index - 1)
            }
        }

        return gamma * epsilon
    }

    fun determine(input: List<String>, keepMostCommon: Boolean): Int {
        val bit1 = if (keepMostCommon) '1' else '0'
        val bit2 = if (!keepMostCommon) '1' else '0'

        val candidates = input.toMutableList()

        for (index in input[0].indices) {
            if (candidates.size == 1) {
                break
            }

            val sums = bitSums(candidates)
            val keepBit = when {
                sums[index] * 2 == candidates.size -> bit1
                sums[index] > candidates.size / 2 -> bit1
                else -> bit2
            }
            candidates.removeIf { it[index] != keepBit }
        }

        return Integer.parseInt(candidates.single(), 2)
    }

    fun part2(input: List<String>): Int {
        return determine(input, true) * determine(input, false)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
