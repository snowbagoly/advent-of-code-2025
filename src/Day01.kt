import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val result = input.fold(Pair(50,0), { (current, zeros), line ->
            val nextNumber = (100 + current + (if (line[0] == 'L') -1 else 1) * line.substring(1).toInt()) % 100
            Pair(nextNumber, zeros + (if (nextNumber == 0) 1 else 0))
        })
        return result.second
    }

    fun part2(input: List<String>): Int {
        val result = input.fold(Pair(50,0), { (current, zeros), line ->
            val clicks = line.substring(1).toInt()
            val fullTurns = clicks / 100
            val leftoverTurn = clicks % 100
            val nextNumber = (100 + current + (if (line[0] == 'L') -1 else 1) * leftoverTurn) % 100
            val turnedOverInLastTurn = current != 0 && abs(leftoverTurn) > current && line[0] == 'L' || current + leftoverTurn > 100 && line[0] == 'R'
            Pair(nextNumber, zeros + fullTurns + (if (nextNumber == 0) 1 else 0) + (if (turnedOverInLastTurn) 1 else 0))
        })
        return result.second
    }

    val testInput = readInput("Day01_example")
    check(part1(testInput) == 3)

    val input = readInput("Day01")
    part1(input).println()

    check(part2(testInput) == 6)
    part2(input).println()
}
