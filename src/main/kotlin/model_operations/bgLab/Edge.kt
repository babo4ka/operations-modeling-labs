package model_operations.bgLab

data class Edge(
    val from: Int,
    val to: Int,
    var capacity: Int,
    var cost: Float
)