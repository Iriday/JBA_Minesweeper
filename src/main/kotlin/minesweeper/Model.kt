package minesweeper

import kotlin.random.Random
import minesweeper.MoveResult.*

private val mine = -1
private val empty = 0
private val mark = -2
private val hintNumbs = 1..8

private lateinit var gridSizeRange: IntRange
private lateinit var initialField: Array<Array<Int>>
private lateinit var winFieldState: Array<Array<Int>>
private lateinit var mutableField: Array<Array<Int>>
private var initPerformed = false
private var gameOver = false

fun initAndGetGameField(gridSize: Int, numOfMines: Int): Array<Array<Int>> {
    gridSizeRange = 0 until gridSize

    initialField = generateSafeField(gridSize)
    addMinesRandPlaces(initialField, numOfMines)
    addHintsOnField(initialField)
    winFieldState = replaceMinesWithMarks(initialField)
    mutableField = deepArrayCopy(initialField)

    initPerformed = true
    gameOver = false

    return mutableField
}

fun makeMove(x: Int, y: Int): MoveResult {
    if (!initPerformed) throw IllegalStateException("Call initAndGetGameField first")
    if (gameOver) throw IllegalStateException("The game is over, to start again call initAndGetGameField again")
    if (x !in gridSizeRange || y !in gridSizeRange) throw IllegalArgumentException("Incorrect coords")

    var attemptToMarkHint = false
    when (mutableField[y][x]) {
        empty, mine -> mutableField[y][x] = mark
        mark -> mutableField[y][x] = initialField[y][x]
        in hintNumbs -> attemptToMarkHint = true
        else -> throw RuntimeException("Incorrect fieldItem found")
    }
    if (attemptToMarkHint) {
        return ATTEMPT_TO_MARK_HINT
    }
    if (deepContentEquals(winFieldState, mutableField)) {
        gameOver = true
        return WIN
    }

    return MARKED_UNMARKED
}

private fun generateSafeField(size: Int): Array<Array<Int>> {
    return Array(size) { Array(size) { empty } }
}

private fun addMinesRandPlaces(field: Array<Array<Int>>, numOfMines: Int) {
    val coords = getCoordsOfEmptySpots(field)
    if (numOfMines > coords.size) throw IllegalArgumentException("Not enough space for $numOfMines mines")
    for (i in 0 until numOfMines) {
        val newMineCoords = coords.removeAt(Random.nextInt(coords.size))
        addItemOnField(field, mine, newMineCoords.first, newMineCoords.second)
    }
}

private fun addHintsOnField(field: Array<Array<Int>>) {
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

private fun replaceMinesWithMarks(field: Array<Array<Int>>): Array<Array<Int>> {
    return field.map { row -> row.map { fieldItem -> if (fieldItem == mine) mark else fieldItem }.toTypedArray() }.toTypedArray()
}
