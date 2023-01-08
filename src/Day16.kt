import java.lang.IllegalStateException
import java.lang.StringBuilder
import java.math.BigInteger

fun main() {
    abstract class Packet(val version: Int) {
        open fun versionSum() = version
        abstract fun evaluate(): Long
    }

    class Literal(version: Int, val value: Long) : Packet(version) {
        override fun evaluate() = value
    }

    class Operator(version: Int, val typeId: Int, val operands: List<Packet>) : Packet(version) {
        override fun versionSum() = version + operands.sumOf { it.versionSum() }
        override fun evaluate(): Long = when (typeId) {
            0 -> operands.sumOf { it.evaluate() }
            1 -> operands.fold(1) { acc, packet -> acc * packet.evaluate() }
            2 -> operands.minOf { it.evaluate() }
            3 -> operands.maxOf { it.evaluate() }
            5 -> if (operands.first().evaluate() > operands.last().evaluate()) 1 else 0
            6 -> if (operands.first().evaluate() < operands.last().evaluate()) 1 else 0
            7 -> if (operands.first().evaluate() == operands.last().evaluate()) 1 else 0
            else -> throw IllegalStateException()
        }
    }

    class Parser(input: String) {
        val bits = BigInteger(input, 16).toString(2).padStart(input.length * 4, '0')
        var offset = 0

        fun String.read(from: Int, length: Int): Int {
            return substring(from, from + length).toInt(2)
        }

        fun parse(): Packet {
            val version = bits.read(offset + 0, 3)
            val typeId = bits.read(offset + 3, 3)
            offset += 6

            return if (typeId == 4) {
                Literal(version, parseLiteral(bits))
            } else {
                val lengthType = bits.read(offset++, 1)
                val length = bits.read(offset, if (lengthType == 0) 15 else 11)
                offset += if (lengthType == 0) 15 else 11

                val operands = buildList {
                    if (lengthType == 0) {
                        val lastOffset = offset
                        while (offset - lastOffset < length) add(parse())
                    } else {
                        repeat(length) { add(parse()) }
                    }
                }

                Operator(version, typeId, operands)
            }
        }

        fun parseLiteral(bits: String): Long {
            var hasNext = true
            val buffer = StringBuilder()

            while (hasNext) {
                hasNext = bits.read(offset, 1) == 1
                buffer.append(bits.substring(offset + 1, offset + 5))
                offset += 5
            }

            return buffer.toString().toLong(2)
        }
    }


    fun part1(input: List<String>): Int {
        val parser = Parser(input.first())
        val packet = parser.parse()
        return packet.versionSum()
    }

    fun part2(input: List<String>): Long {
        val parser = Parser(input.first())
        val packet = parser.parse()
        return packet.evaluate()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(listOf("D2FE28")) == 6)
    check(part1(listOf("38006F45291200")) == 9)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
