package model_operations.bgLab

fun main(){

    val verticesCount = 6

    val edges = mutableListOf<Edge>()
    val adj = Array(verticesCount) { mutableListOf<Edge>() }

    fun addEdge(from: Int, to: Int, capacity: Int, cost: Float) {
        val edge = Edge(from, to, capacity, cost)
        edges.add(edge)
        adj[from].add(edge)
    }

    fun minCostFlow(source: Int, sink: Int, flow: Int): Pair<Int, Float> {
        var totalCost = 0.0f
        var totalFlow = 0

        while (totalFlow < flow) {
            // Реализация алгоритма поиска кратчайшего пути с уменьшенной стоимостью
            val distances = IntArray(verticesCount) { Int.MAX_VALUE }
            val parent = Array(verticesCount) { -1 }
            val inQueue = BooleanArray(verticesCount)
            val queue = ArrayDeque<Int>()

            distances[source] = 0
            queue.addLast(source)
            inQueue[source] = true

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                inQueue[current] = false

                for (edge in adj[current]) {
                    if (edge.capacity > 0 && distances[edge.to] > distances[current] + edge.cost) {
                        //distances[edge.to] = distances[current] + edge.cost
                        parent[edge.to] = edges.indexOf(edge)

                        if (!inQueue[edge.to]) {
                            queue.addLast(edge.to)
                            inQueue[edge.to] = true
                        }
                    }
                }
            }

            if (distances[sink] == Int.MAX_VALUE) break // Нет больше пути

            // Определение увеличения потока через найдённый путь
            var incrementFlow = flow - totalFlow
            var cur = sink
            while (cur != source) {
                val edgeIndex = parent[cur]
                val edge = edges[edgeIndex]
                incrementFlow = Math.min(incrementFlow, edge.capacity)
                cur = edge.from
            }

            // Обновление рёбер
            cur = sink
            while (cur != source) {
                val edgeIndex = parent[cur]
                val edge = edges[edgeIndex]
                edge.capacity -= incrementFlow
                totalCost += incrementFlow * edge.cost
                cur = edge.from
            }
            totalFlow += incrementFlow
        }

        return Pair(totalFlow, totalCost)
    }


//    addEdge(0, 1, 1, 1)
//    addEdge(1, 2, 2, 1)
//    addEdge(1, 4, 1, 1)
//    addEdge(2, 3, 2, 2)
//    addEdge(3, 6, 2, 2)
//    addEdge(4, 5, 1, 2)
//    addEdge(4, 6, 1, 2)
//    addEdge(5, 8, 1, 1)
//    addEdge(6, 7, 1, 2)
//    addEdge(7, 8, 1, 1)

    addEdge(0, 1, 1, 1.0f)
    addEdge(0, 3, 1, 1.0f)
    addEdge(1, 2, 1, 2.0f)
    addEdge(1, 4, 2, 2.0f)
    addEdge(2, 5, 1, 1.0f)
    addEdge(3, 2, 1, 2.0f)
    addEdge(4, 5, 2, 2.0f)

    val (maxFlow, minCost) = minCostFlow(0, 5, 2)
    println("Максимальный поток: $maxFlow, Минимальная стоимость: $minCost")
}