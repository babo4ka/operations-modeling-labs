package model_operations.bgLab

data class Edge(
    val from: Int,
    val to: Int,
    val capacity: Int,
    val cost: Int,
    var flow: Int = 0
)