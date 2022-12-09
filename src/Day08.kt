fun main() {
    val easyDigits = mapOf(
        2 to 1,
        4 to 4,
        3 to 7,
        7 to 8,
    )

    fun part1(input: List<String>): Int {
        val characters = input.flatMap { it.split(" | ")[1].split(" ") }
        return characters.count { it.length in easyDigits }
    }

    class Mapping {
        private val mapping = mutableMapOf<Set<Char>, Int>()
        private val inverseMapping = mutableMapOf<Int, Set<Char>>()

        fun insert(value: Int, chars: Set<Char>) {
            mapping[chars] = value
            inverseMapping[value] = chars

            check(mapping.size == inverseMapping.size)
        }

        operator fun get(chars: Set<Char>) = mapping.getValue(chars)
        operator fun get(value: Int) = inverseMapping.getValue(value)
        operator fun contains(chars: Set<Char>) = chars !in mapping

        override fun toString() =
            mapping.entries.sortedBy { it.value }.joinToString { "${it.value}=${it.key.joinToString("")}" }
    }

    fun <T> MutableList<T>.removeFirst(filter: (T) -> Boolean) = removeAt(indexOfFirst(filter))

    fun buildMapping(allDigits: Iterable<Set<Char>>): Mapping {
        val all = allDigits.distinct().toMutableList()
        check(all.size == 10)

        val mapping = Mapping()

        for ((count, value) in easyDigits) {
            mapping.insert(value, all.removeFirst { it.size == count })
        }

        // 3 has 5 chars and contains all of 1
        mapping.insert(3, all.removeFirst { it.size == 5 && it.containsAll(mapping[1]) })

        // 9 has 6 chars and contains union of 3 & 4
        mapping.insert(9, all.removeFirst { it.size == 6 && it.containsAll(mapping[3] union mapping[4]) })

        // determine char that represents e (the char that differentiates 5 from 6)
        val eChar = mapping[8] subtract mapping[9]

        // 6 has 6 chars, 5 has 5 chars, and they only differ by the eChar
        outer@ for (fiveChar in all.filter { it.size == 5 }) {
            for (sixChar in all.filter { it.size == 6 }) {
                if (sixChar subtract fiveChar == eChar) {
                    mapping.insert(6, sixChar)
                    mapping.insert(5, fiveChar)
                    all.remove(sixChar)
                    all.remove(fiveChar)
                    break@outer
                }
            }
        }

        // 2 has 5 chars and is not already mapped
        mapping.insert(2, all.removeFirst { it.size == 5 })

        // 0 has 6 chars and is not already mapped
        mapping.insert(0, all.removeFirst { it.size == 6 })

        return mapping
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        for (line in input) {
            val (test, output) = line.split(" | ").map { it.split(" ").map(String::toSet) }
            val mapping = buildMapping(test + output)

            sum += output.joinToString("") { mapping[it].toString() }.toInt()
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
