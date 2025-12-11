fun adjacents(pos: Pair<Int, Int>, rollPositions: Set<Pair<Int,Int>>): List<Pair<Int,Int>> {
    return (-1..1).flatMap { i ->
        (-1..1).map { j ->
            Pair(pos.first + i,pos.second + j)
        } }.filter { p -> p != pos && rollPositions.contains(p)}
}

fun calculateRollNeighbors(rollPositions: Set<Pair<Int,Int>>): Map<Pair<Int,Int>, Int> {
    return rollPositions.associate { pos ->
        Pair(pos, adjacents(pos, rollPositions).size)
    }
}

fun getRollPositions(input: List<String>): Set<Pair<Int,Int>> {
    return input.flatMapIndexed { i, line ->
        line.mapIndexedNotNull { j, c -> if (c == '@') Pair(i,j) else null }
    }.toSet()
}

fun main() {
    fun part1(input: List<String>): Int {
        return calculateRollNeighbors(getRollPositions(input)).count { (_, c) -> c < 4 }
    }

    fun part2(input: List<String>): Int {
        val rollPositions = getRollPositions(input).toMutableSet()
        val rollNeighbors = calculateRollNeighbors(rollPositions).toMutableMap()
        var removed = 0
        var toBeRemoved = rollNeighbors.filter { (_, c) -> c < 4 }.keys
        while (toBeRemoved.isNotEmpty()) {
            removed += toBeRemoved.size
            toBeRemoved.forEach { pos ->
                adjacents(pos, rollPositions).forEach {
                    rollNeighbors[it] = rollNeighbors[it]!! - 1
                }
            }
            rollPositions.removeAll(toBeRemoved)
            toBeRemoved.forEach { pos -> rollNeighbors.remove(pos) }
            toBeRemoved = rollNeighbors.filter { (_, c) -> c < 4 }.keys
        }

        return removed
    }



    val testInput = readInput("Day04_example")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()

    check(part2(testInput) == 43)
    part2(input).println()
}