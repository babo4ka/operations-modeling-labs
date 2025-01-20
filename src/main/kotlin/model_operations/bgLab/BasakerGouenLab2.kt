package model_operations.bgLab


fun main(){

    val vertices = 6
    val edges = mutableListOf<Edge>()
    val edgesIndexes = mutableMapOf<Pair<Int, Int>, Int>()

    fun addEdge(from: Int, to: Int, capacity: Int, cost: Float) {
        edges.add(Edge(from, to, capacity, cost))
        edgesIndexes[(from to to)] = edges.size-1
        edges.add(Edge(to, from, 0, Float.POSITIVE_INFINITY))
        edgesIndexes[(to to from)] = edges.size-1
    }

    fun printMatrix(){

        val maxLengthEachCol: () -> IntArray ={

            val arr = IntArray(vertices){1}

            for(i in 0..<vertices){
                for(j in 0..<vertices){

                    if(edgesIndexes[j to i] != null){
                        val edge = edges[edgesIndexes[j to i]!!]

                        if(edge.cost != Float.POSITIVE_INFINITY)
                            if(edge.cost.toString().length > arr[i]) arr[i] = edge.cost.toInt().toString().length
                    }
                }
            }

            arr
        }

        val maxLengths = maxLengthEachCol()
        print(" |")
        for(i in 0..<vertices){
            print("$i${" ".repeat(maxLengths[i] - 1)}|")
        }

        println()

        for(i in 0..<vertices){
            print("$i|")
            for(j in 0..<vertices){

                if(edgesIndexes[i to j] == null){
                    print("∞${" ".repeat(maxLengths[j] - 1)}|")
                }else{
                    val edge = edges[edgesIndexes[i to j]!!]
                    val line = "${if(edge.cost == Float.POSITIVE_INFINITY)"∞" else edge.cost.toInt()}"
                    print("$line${" ".repeat(maxLengths[j] - line.length)}|")
                }
            }
            println()
        }

        println()
    }


    fun minCostFlow(from:Int, to:Int, maxFlow:Int):Triple<IntArray, Float, Int>{
        var totalFlow = 0


        fun minimalWay():Pair<Float, IntArray>{
            val next = Array(vertices){IntArray(vertices)}


            val fillDist:() -> Array<FloatArray> = {
                val distMatrix = Array(vertices){FloatArray(vertices){Float.POSITIVE_INFINITY}}

                for (i in 0..<vertices){
                    distMatrix[i][i] = 0.0f
                    next[i][i] = i
                }

                edges.forEach {
                    distMatrix[it.from][it.to] = it.cost
                    next[it.from][it.to] = it.to
                }

                distMatrix
            }


            val dist = fillDist()


            for(k in 0..<vertices){
                for(i in 0..<vertices){
                    for (j in 0..<vertices){
                        if(dist[i][k] + dist[k][j] < dist[i][j]){
                            dist[i][j] = dist[i][k] + dist[k][j]
                            next[i][j] = next[i][k]
                        }
                    }
                }
            }

            val getMinimalWay: (next:Array<IntArray>) -> IntArray = {
                var current = from
                if(dist[current][to] == Float.POSITIVE_INFINITY){
                    intArrayOf()
                }

                val way = mutableListOf(current)

                while(current != to){
                    current = next[current][to]
                    way.add(current)
                }

                way.toIntArray()
            }

            return dist[from][to] to getMinimalWay(next)
        }


        var minimalWayPrice = 0.0f
        var minimalWayArr:IntArray = intArrayOf()

        fun printWay(){
            println("Минимальный поток: ${minimalWayArr.joinToString("->")}")
            println("Длина потока: $minimalWayPrice")
            println("Величина потока: $totalFlow")
            println()
        }

        while(totalFlow < maxFlow){

            val (minWayPrice, minWay) = minimalWay()
            minimalWayPrice = minWayPrice
            minimalWayArr = minWay

            val currEdgesSt = mutableListOf<Edge>()
            val currentEdgeBa = mutableListOf<Edge>()

            for(i in 0..<minWay.size-1){
                val currFrom = minWay[i]
                val currTo = minWay[i+1]
                currEdgesSt.add(edges[edgesIndexes[currFrom to currTo]!!])
                currentEdgeBa.add(edges[edgesIndexes[currTo to currFrom]!!])
            }

            val currFlow = currEdgesSt.minOf { it.capacity }

            currEdgesSt.forEachIndexed { i, e ->
                if(currFlow == e.capacity){
                    currentEdgeBa[i].cost = -e.cost
                    currentEdgeBa[i].capacity = e.capacity
                    e.cost = Float.POSITIVE_INFINITY
                }else if(currFlow >= 0 && currFlow < e.capacity){
                    currentEdgeBa[i].cost = -e.cost
                    currentEdgeBa[i].capacity = currFlow
                    e.capacity -= currFlow
                }
            }

            totalFlow += currFlow

            printWay()

            println("Изменённая матрица: ")
            printMatrix()
        }


        return Triple(minimalWayArr, minimalWayPrice, totalFlow)
    }


    addEdge(0, 1, 1, 1.0f)
    addEdge(0, 3, 1, 1.0f)
    addEdge(1, 2, 1, 2.0f)
    addEdge(1, 4, 2, 2.0f)
    addEdge(2, 5, 1, 1.0f)
    addEdge(3, 2, 1, 2.0f)
    addEdge(4, 5, 2, 2.0f)

    println("Изначальная матрица: ")
    printMatrix()

    val (minFlow, flowL, v) = minCostFlow(0, 5, 2)

    println("Минимальный поток: ${minFlow.joinToString("->")}")
    println("Длина потока: $flowL")
    println("Величина потока: $v")


}