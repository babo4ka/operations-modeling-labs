package model_operations.gomoriMethodLab

import java.math.RoundingMode
import kotlin.math.truncate

fun makeInt(a:Double):Double{
    val decimaled = a.toBigDecimal().setScale(3, RoundingMode.HALF_UP).toDouble()
    if(decimaled - truncate(decimaled) == .999 || decimaled - truncate(decimaled) == .000){
        return decimaled.toBigDecimal().setScale(0, RoundingMode.HALF_UP).toDouble()
    }

    return a
}

fun printData(a:Array<DoubleArray>, basisVars:MutableMap<String, Int>, freeVars:MutableMap<String, Int>){
    val maxl = mutableMapOf<Int, Int>()
    a.forEachIndexed {id, it ->
        it.forEachIndexed { index, el ->
            val l = el.toBigDecimal().setScale(3, RoundingMode.HALF_UP).toDouble().toString().length
            if(maxl[index] == null){
                maxl[index] = l
            }else if(l > maxl[index]!!){
                maxl[index] = l
            }
        }
    }

    val freeVarsClone = freeVars.toMutableMap()
    val basisVarsClone = basisVars.toMutableMap()

    freeVarsClone["b"] = freeVars.size
    basisVarsClone["z"] = basisVars.size

    val sortedfree = freeVarsClone.toList().sortedBy {
        (_, v) -> v }.toMap()
    val sortedBasis = basisVarsClone.toList().sortedBy {
        (_, v) -> v }.toMap()

    print("${" ".repeat(2)}|")
    sortedfree.keys.forEachIndexed { id, it ->
        print("$it${" ".repeat(maxl[id]!! - it.length)}|")
    }

    println()
    a.forEachIndexed {i, it->
        print("${sortedBasis.keys.toList()[i]}${" ".repeat(3 - sortedBasis.keys.toList()[i].length)}|")
        it.forEachIndexed { id, el ->
            print("${el.toBigDecimal().setScale(3, RoundingMode.HALF_UP).toDouble()}${" ".repeat(maxl[id]!! - makeInt(el.toBigDecimal().setScale(3, RoundingMode.HALF_UP).toDouble()).toString().length)}|")
        }
        println()
    }

}