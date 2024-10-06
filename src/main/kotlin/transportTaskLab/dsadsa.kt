package transportTaskLab

fun main(){
    var a = mutableListOf(0, 0, 2, 3, 4, 0)
    a.remove(0)
    a.removeAll { e -> e == 0 }

    println(a.joinToString())
}