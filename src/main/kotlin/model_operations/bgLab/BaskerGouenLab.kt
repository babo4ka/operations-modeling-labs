package model_operations.bgLab

fun main(){

    val vertices = 6
    val graph = mutableListOf<MutableList<Edge>>()
    val potential = IntArray(vertices) { 0 }


    for (i in 0..<vertices) {
        graph.add(mutableListOf())
    }

    fun addEdge(from: Int, to: Int, capacity: Int, cost: Int) {
        graph[from].add(Edge(from, to, capacity, cost))
        graph[to].add(Edge(to, from, 0, -cost))
    }

    fun bellmanFord(source: Int) {
        val dist = IntArray(vertices) { Int.MAX_VALUE / 2 }
        dist[source] = 0

        for (i in 0..<vertices - 1) {
            for (u in 0..<vertices) {
                for (edge in graph[u]) {
                    if (edge.flow < edge.capacity &&
                        dist[u] + edge.cost < dist[edge.to]) {
                        dist[edge.to] = dist[u] + edge.cost
                    }
                }
            }
        }

        for (i in 0..<vertices) {
            potential[i] += dist[i]
        }
    }

    fun dijkstra(source: Int): Pair<IntArray, IntArray> {
        val dist = IntArray(vertices) { Int.MAX_VALUE / 2 }
        val parent = IntArray(vertices) { -1 }
        dist[source] = 0

        val priorityQueue = java.util.PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
        priorityQueue.add(Pair(0, source))

        while (priorityQueue.isNotEmpty()) {
            val (currentDist, u) = priorityQueue.poll()

            if (currentDist > dist[u]) continue

            for (edge in graph[u]) {
                if (edge.flow < edge.capacity) {
                    val cost = edge.cost + potential[u] - potential[edge.to]
                    if (dist[u] + cost < dist[edge.to]) {
                        dist[edge.to] = dist[u] + cost
                        parent[edge.to] = u
                        priorityQueue.add(Pair(dist[edge.to], edge.to))
                    }
                }
            }
        }

        return Pair(dist, parent)
    }


    fun minCostMaxFlow(source: Int, sink: Int, maxFlow: Int): Pair<Int, Int> {
        bellmanFord(source) // инициализация потенциалов

        var totalFlow = 0
        var totalCost = 0

        while (totalFlow < maxFlow) {
            val (dist, parent) = dijkstra(source)

            if (dist[sink] == Int.MAX_VALUE / 2) break // поток больше не может расти

            // Обновляем потенциалы
            for (i in 0 until vertices) {
                if (dist[i] < Int.MAX_VALUE / 2) {
                    potential[i] += dist[i]
                }
            }

            // Определяем минимальную емкость по найденному пути
            var currentFlow = Int.MAX_VALUE
            var v = sink
            while (v != source) {
                val u = parent[v]
                val edge = graph[u].first { it.to == v }
                currentFlow = minOf(currentFlow, edge.capacity - edge.flow)
                v = u
            }

            // Обновляем ребра с учетом добавленного потока
            v = sink
            while (v != source) {
                val u = parent[v]
                val edge = graph[u].first { it.to == v }
                edge.flow += currentFlow

                val backEdge = graph[v].first { it.to == u }
                backEdge.flow -= currentFlow

                totalCost += edge.cost * currentFlow
                v = u
            }

            totalFlow += currentFlow
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

    addEdge(0, 1, 1, 1)
    addEdge(0, 3, 1, 1)
    addEdge(1, 2, 1, 2)
    addEdge(1, 4, 2, 2)
    addEdge(2, 5, 1, 1)
    addEdge(3, 2, 1, 2)
    addEdge(4, 5, 2, 2)

    val (flow, cost) = minCostMaxFlow(0, 3, 2) // Из S в T
    println("Максимальный поток: $flow, Минимальная стоимость: $cost")
}