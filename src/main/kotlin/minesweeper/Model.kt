package minesweeper

import kotlin.random.Random


fun generateSafeField(size: Int): Array<Array<String>> {
    return Array(size) { Array(size) { "." } }
}

fun addMinesRandPlaces(field: Array<Array<String>>, numOfMines: Int) {
    val coords = getCoordsOfEmptySpots(field)
    if (numOfMines > coords.size) throw IllegalArgumentException("Not enough space for $numOfMines mines")
    for (i in 0 until numOfMines) {
        val newMineCoords = coords.removeAt(Random.nextInt(coords.size))
        addMine(field, newMineCoords.first, newMineCoords.second)
    }
}

private fun addMine(field: Array<Array<String>>, coordRow: Int, coordCol: Int) {
    field[coordRow][coordCol] = "X"
}

private fun getCoordsOfEmptySpots(field: Array<Array<String>>): MutableList<Pair<Int, Int>> {
    val coords = mutableListOf<Pair<Int, Int>>()
    field.forEachIndexed { i, val_i -> val_i.forEachIndexed { j, val_ij -> if (val_ij == ".") coords.add(Pair(i, j)) } }
    return coords
}
