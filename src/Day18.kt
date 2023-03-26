import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlin.math.ceil

fun main() {
    abstract class SNum {
        var parent: SNum? = null

        abstract fun magnitude(): Long
    }

    class SPair(var left: SNum, var right: SNum) : SNum() {
        init {
            left.parent = this
            right.parent = this
        }

        override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
        override fun toString() = "[$left,$right]"
    }

    class SLiteral(var value: Long) : SNum() {
        override fun magnitude() = value
        override fun toString() = value.toString()
    }

    fun JsonElement.toSNum(): SNum = when (this) {
        is JsonPrimitive -> SLiteral(this.asLong)
        is JsonArray -> SPair(get(0).toSNum(), get(1).toSNum())
        else -> error("nope")
    }

    fun String.parse() = JsonParser.parseString(this).toSNum()

    fun SNum.nestedPair(depth: Int): SPair? {
        if (this !is SPair) return null
        if (depth == 0) return this
        return left.nestedPair(depth - 1) ?: right.nestedPair(depth - 1)
    }

    fun SNum.findLiteral(descendLeft: Boolean): SLiteral = when (this) {
        is SLiteral -> this
        is SPair -> if (descendLeft) left.findLiteral(true) else right.findLiteral(false)
        else -> error("nope")
    }

    fun SNum.findLiteralNeighbor(left: Boolean): SLiteral? {
        val parent = parent as SPair? ?: return null
        return if (left && this == parent.right) parent.left.findLiteral(false)
        else if (!left && this == parent.left) parent.right.findLiteral(true)
        else parent.findLiteralNeighbor(left)
    }

    fun SNum.findLiteralOver9(): SLiteral? {
        if (this is SLiteral) return if (value > 9) this else null
        if (this is SPair) return left.findLiteralOver9() ?: right.findLiteralOver9()
        error("nope")
    }

    fun SNum.replaceWith(sNum: SNum) {
        val parent = parent as SPair
        if (this == parent.left) parent.left = sNum
        else parent.right = sNum
        sNum.parent = parent
    }

    fun SNum.reduce(): SNum {
        while (true) {
            val nested = nestedPair(4)
            if (nested != null) {
                nested.findLiteralNeighbor(true)?.let { it.value += (nested.left as SLiteral).value }
                nested.findLiteralNeighbor(false)?.let { it.value += (nested.right as SLiteral).value }
                nested.replaceWith(SLiteral(0))
                continue
            }

            val over9 = findLiteralOver9()
            if (over9 != null) {
                over9.replaceWith(
                    SPair(
                        SLiteral(over9.value / 2),
                        SLiteral(ceil(over9.value / 2.0).toLong())
                    )
                )

                continue
            }

            return this
        }
    }

    operator fun SNum.plus(sNum: SNum) = SPair(this, sNum).reduce()

    fun part1(input: List<String>): Long {
        val snums = input.map { it.parse() }

        val sum = snums.reduce { a, b -> a + b }
        return sum.magnitude()
    }

    fun part2(input: List<String>): Long {
        var max = 0L

        for (a in input) {
            for (b in input) {
                if (a == b) continue
                max = maxOf(max, a.parse().plus(b.parse()).magnitude())
                max = maxOf(max, b.parse().plus(a.parse()).magnitude())
            }
        }

        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140L)
    check(part2(testInput) == 3993L)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
