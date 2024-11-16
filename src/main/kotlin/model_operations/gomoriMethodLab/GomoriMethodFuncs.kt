package model_operations.gomoriMethodLab

val Z:(Int, Int) -> Int = {x1:Int, x2:Int -> 3*x1 + 2*x2}

val constraints = mutableListOf(
    mutableListOf(0, 2, 5),
    mutableListOf(2, -3, 7),
    mutableListOf(4, 3, 8),
    mutableListOf(-3, 1, 8)
)

val constraintsSigns = listOf(">=", "<=", ">=", "<=")

fun withinConstraints(x1:Double, x2:Double):Boolean{
    return if(constraintsSigns[0] == ">=") constraints[0][0]*x1 + constraints[0][1]*x2 >= constraints[0][2] else
        constraints[0][0]*x1 + constraints[0][1]*x2 <= constraints[0][2]
                &&
    if(constraintsSigns[1] == ">=") constraints[1][0]*x1 + constraints[1][1]*x2 >= constraints[1][2] else
        constraints[1][0]*x1 + constraints[1][1]*x2 <= constraints[1][2]
                &&
    if(constraintsSigns[2] == ">=") constraints[2][0]*x1 + constraints[2][1]*x2 >= constraints[2][2] else
        constraints[2][0]*x1 + constraints[2][1]*x2 <= constraints[2][2]
                &&
    if(constraintsSigns[3] == ">=") constraints[3][0]*x1 + constraints[3][1]*x2 >= constraints[3][2] else
        constraints[3][0]*x1 + constraints[3][1]*x2 <= constraints[3][2]
}

val free = mutableListOf("x1", "x2")
val basis = mutableListOf<String>()
val minusOneCouples = mutableListOf<Pair<String, String>>()

fun createBasis(){
    constraintsSigns.forEach{
        basis.add("${if(it==">=")"-" else ""}x${free.size + basis.size + 1}")
    }

    basis.forEach {
        if(it.startsWith("-")){
            val temp = it.substring(1)
            val nextXName = "x${free.size + basis.size + 1}"
            basis[basis.indexOf(it)] = nextXName
            free.add(temp)
            minusOneCouples.add(temp to nextXName)
        }
    }
}

fun initiateTable():MutableList<MutableList<Double>>?{
    val table = constraints.toMutableList()




    return null
}

fun main(){
}