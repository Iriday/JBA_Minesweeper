package minesweeper

import kotlin.random.Random

val mine = -1
val empty = 0

fun generateSafeField(size: Int): Array<Array<Int>> {
    return Array(size) { Array(size) { empty } }
}

fun addMinesRandPlaces(field: Array<Array<Int>>, numOfMines: Int) {
    val coords = getCoordsOfEmptySpots(field)
    if (numOfMines > coords.size) throw IllegalArgumentException("Not enough space for $numOfMines mines")
    for (i in 0 until numOfMines) {
        val newMineCoords = coords.removeAt(Random.nextInt(coords.size))
        addItemOnField(field, mine, newMineCoords.first, newMineCoords.second)
    }
}

fun addHintsOnField(field: Array<Array<Int>>) {
    fun calculateNumOfMinesAround(field: Array<Array<Int>>, i: Int, j: Int): Int {
        val rows = field.size
        val cols = field[0].size
        var mines = 0

        if (i - 1 != -1) { // top
            mines += isMine(field[i - 1][j])
        }
        if (i - 1 != -1 && j + 1 != cols) { // top right
            mines += isMine(field[i - 1][j + 1])
        }
        if (j + 1 != cols) { // right
            mines += isMine(field[i][j + 1])
        }
        if (i + 1 != rows && j + 1 != cols) {  // bottom right
            mines += isMine(field[i + 1][j + 1])
        }
        if (i + 1 != rows) { // bottom
            mines += isMine(field[i + 1][j])
        }
        if (i + 1 != rows && j - 1 != -1) { // left bottom
            mines += isMine(field[i + 1][j - 1])
        }
        if (j - 1 != -1) { // left
            mines += isMine(field[i][j - 1])
        }
        if (i - 1 != -1 && j - 1 != -1) { // left top
            mines += isMine(field[i - 1][j - 1])
        }
        return mines
    }

    for (i in field.indices) {
        for (j in field[i].indices) {
            if (field[i][j] != empty) continue

            val numOfMines = calculateNumOfMinesAround(field, i, j)
            if (numOfMines > 0) addItemOnField(field, numOfMines, i, j) // add hint
        }
    }
}

private fun addItemOnField(field: Array<Array<Int>>, filedItem: Int, coordRow: Int, coordCol: Int) {
    field[coordRow][coordCol] = filedItem
}

private fun getCoordsOfEmptySpots(field: Array<Array<Int>>): MutableList<Pair<Int, Int>> {
    val coords = mutableListOf<Pair<Int, Int>>()
    field.forEachIndexed { i, val_i -> val_i.forEachIndexed { j, val_ij -> if (val_ij == empty) coords.add(Pair(i, j)) } }
    return coords
}

private fun isMine(fieldItem: Int): Int = if (fieldItem == mine) 1 else 0
