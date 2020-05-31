package minesweeper

import kotlin.random.Random

fun main() {
    val filed = generateSafeField(10)
    addMinesRandPlaces(filed, 10)
    printFiled(filed)
}

fun generateSafeField(size: Int): Array<Array<String>> {
    return Array(size) { Array(size) { "." } }
}

fun addMine(filed: Array<Array<String>>, mineRow: Int, mineCol: Int) {
    filed[mineRow][mineCol] = "X"
}

fun addMineRandPlace(filed: Array<Array<String>>) {
    addMine(filed, Random.nextInt(filed.size), Random.nextInt(filed.size))
}

fun addMinesRandPlaces(filed: Array<Array<String>>, numberOfMines: Int) {
    for (i in 0 until numberOfMines) addMineRandPlace(filed)
}

fun printFiled(filed: Array<Array<String>>) {
    filed.forEach { row -> row.forEach { filedItem -> print(filedItem) }; println() }
}
