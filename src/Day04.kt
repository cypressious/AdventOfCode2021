fun main() {

    class Board(
        val numbers: List<IntArray>
    ) {
        val marked = Array(numbers.size) { BooleanArray(numbers[0].size) }

        fun mark(n: Int) {
            for ((rowIndex, row) in numbers.withIndex()) {
                for ((columnIndex, x) in row.withIndex()) {
                    if (x == n) {
                        marked[rowIndex][columnIndex] = true
                        return
                    }
                }
            }
        }

        fun hasWinner(): Boolean {
            return marked.any { row -> row.all { it } } ||
                    marked[0].indices.any { rowIndex -> marked.all { row -> row[rowIndex] } }
        }

        fun sumOfUnmarked(): Int {
            var sum = 0
            for ((rowIndex, row) in numbers.withIndex()) {
                for ((columnIndex, x) in row.withIndex()) {
                    if (!marked[rowIndex][columnIndex]) {
                        sum += x
                    }
                }
            }
            return sum
        }
    }

    fun parse(input: List<String>): Pair<List<Int>, List<Board>> {
        val numbers = input[0].split(',').map(String::toInt)

        val boards = mutableListOf<Board>()
        var currentBoard = mutableListOf<IntArray>()
        for (line in input.drop(2)) {
            if (line.isBlank()) {
                boards += Board(currentBoard)
                currentBoard = mutableListOf()
            } else {
                currentBoard += line.split(' ').filter { it.isNotBlank() }.map(String::toInt).toIntArray()
            }
        }

        if (currentBoard.isNotEmpty()) {
            boards += Board(currentBoard)
        }

        return numbers to boards
    }

    fun part1(input: List<String>): Int {
        val (numbers, boards) = parse(input)

        for (n in numbers) {
            for (board in boards) {
                board.mark(n)
                if (board.hasWinner()) {
                    return board.sumOfUnmarked() * n
                }
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val (numbers, boards) = parse(input)

        for (n in numbers) {
            for (board in boards) {
                board.mark(n)
                if (boards.all { it.hasWinner() }) {
                    return board.sumOfUnmarked() * n
                }
            }
        }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
