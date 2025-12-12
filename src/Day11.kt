import kotlin.collections.forEach

fun buildGraph(input: List<String>): Map<String, Set<String>> {
    return input.associate { it.split(": ")[0] to it.split(" ").drop(1).toSet() }
}

fun calculateNode(
    node: String,
    invertedGraph: Map<String, List<String>>,
    numberOfPaths: MutableMap<String, List<Long>>
) {
    val nodeValues = mutableListOf(0L, 0L, 0L, 0L)

    invertedGraph[node]?.forEach { sourceNode ->
        numberOfPaths[sourceNode]!!.forEachIndexed { index, value ->
            nodeValues[index] += value
        }
    }

    if (node == "fft") {
        nodeValues[1] += nodeValues[0]
        nodeValues[3] += nodeValues[2]
        nodeValues[0] = 0
        nodeValues[2] = 0
    }

    if (node == "dac") {
        nodeValues[2] += nodeValues[0]
        nodeValues[3] += nodeValues[1]
        nodeValues[0] = 0
        nodeValues[1] = 0
    }

    numberOfPaths[node] = nodeValues
}

fun getNumberOfPaths(graph: Map<String, Set<String>>, from: String, to: String): List<Long> {
    val graphCopy = graph.toMutableMap()
    graphCopy["out"] = emptySet()
    val invertedGraph = graph.toList().flatMap { pair -> pair.second.map { second -> Pair(second, pair.first) } }
        .groupBy({ it.first }, { it.second })
    val numberOfPaths = mutableMapOf<String, List<Long>>()
    var noIncomingEdges = setOf(from)
    while (noIncomingEdges.isNotEmpty()) {
        noIncomingEdges.forEach { node ->
            if (node == from) {
                numberOfPaths[node] = mutableListOf(1L, 0L, 0L, 0L)
            } else {
                calculateNode(node, invertedGraph, numberOfPaths)
            }
            graphCopy.remove(node)
        }
        noIncomingEdges =
            graphCopy.keys.filter { key -> graphCopy.values.none { targetList -> targetList.contains(key) } }.toSet()
    }
    return numberOfPaths[to]!!
}

fun main() {
    fun part1(input: List<String>): Long {
        val graph = buildGraph(input)
        return getNumberOfPaths(graph, "you", "out").sum()
    }

    fun part2(input: List<String>): Long {
        val graph = buildGraph(input)
        return getNumberOfPaths(graph, "svr", "out")[3]
    }

    val testInput1 = readInput("Day11_example1")
    check(part1(testInput1) == 5L)

    val input = readInput("Day11")
    part1(input).println()

    val testInput2 = readInput("Day11_example2")
    check(part2(testInput2) == 2L)
    part2(input).println()
}
