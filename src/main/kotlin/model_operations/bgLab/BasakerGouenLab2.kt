package model_operations.bgLab


fun main(){

    val vertices = 6
    val edges = mutableListOf<Edge>()
    val edgesIndexes = mutableMapOf<Pair<Int, Int>, Int>()

    fun addEdge(from: Int, to: Int, capacity: Int, cost: Int) {
        edges.add(Edge(from, to, capacity, cost))
        edgesIndexes[(from to to)] = edges.size-1
        edges.add(Edge(to, from, 0, Int.MAX_VALUE))
        edgesIndexes[(to to from)] = edges.size-1
    }




    fun minCostFlow(from:Int, to:Int, maxFlow:Int):Triple<IntArray, Int, Int>{
        var totalFlow = 0


        fun minimalWay():Pair<Int, IntArray>{
            val next = Array(vertices){IntArray(vertices)}

            val fillDist:() -> Array<IntArray> = {
                val distMatrix = Array(vertices){IntArray(vertices)}

                for (i in 0..<vertices){
                    distMatrix[i][i] = 0
                    next[i][i] = i
                }

                edges.forEach {
                    distMatrix[it.from][it.to] = it.cost
                    next[it.from][it.to] = it.to
                }

                distMatrix
            }


            val dist = fillDist.invoke()


            for(k in 0..<vertices-1){
                for(i in 0..<vertices-1){
                    for (j in 0..<vertices-1){
                        //dist[i][j] = minOf(dist[i][j], dist[i][k] + dist[k][j])
                        if(dist[i][k] + dist[k][j] < dist[i][j]){
                            dist[i][j] = dist[i][k] + dist[k][j]
                            next[i][j] = next[i][k]
                        }
                    }
                }
            }

            val getMinimalWay: (next:Array<IntArray>) -> IntArray = {
                var current = from
                if(dist[current][to] == Int.MAX_VALUE){
                    intArrayOf()
                }

                val way = mutableListOf(current)

                while(current != to){
                    current = next[current][to]
                    way.add(current)
                }

                way.toIntArray()
            }

            return dist[from][to] to getMinimalWay.invoke(next)
        }


        var minimalWayPrice = 0
        var minimalWayArr:IntArray = intArrayOf()

        while(totalFlow < maxFlow){

            val (minWayPrice, minWay) = minimalWay()
            minimalWayPrice = minWayPrice
            minimalWayArr = minWay

            val currEdgesSt = mutableListOf<Edge>()
            val currentEdgeBa = mutableListOf<Edge>()

            for(i in 0..<minWay.size-1){
                val currFrom = i
                val currTo = i+1
                currEdgesSt.add(edges[edgesIndexes[currFrom to currTo]!!])
                currentEdgeBa.add(edges[edgesIndexes[currTo to currFrom]!!])
            }

            val currFlow = currEdgesSt.minOf { it.capacity }

            currEdgesSt.forEachIndexed { i, e ->
                if(currFlow == e.capacity){
                    e.cost = Int.MAX_VALUE
                    currentEdgeBa[i].cost = -e.cost
                    currentEdgeBa[i].capacity = e.capacity
                }else if(currFlow >= 0 && currFlow < e.capacity){
                    currentEdgeBa[i].cost = -e.cost
                    currentEdgeBa[i].capacity = e.capacity - currFlow
                }
            }

            totalFlow += currFlow
        }


        return Triple(minimalWayArr, minimalWayPrice, totalFlow)
    }


    addEdge(0, 1, 1, 1)
    addEdge(0, 3, 1, 1)
    addEdge(1, 2, 1, 2)
    addEdge(1, 4, 2, 2)
    addEdge(2, 5, 1, 1)
    addEdge(3, 2, 1, 2)
    addEdge(4, 5, 2, 2)


    val (a, b, c) = minCostFlow(0, 5, 2)

    println(a)
    println(b)
    println(c)

}