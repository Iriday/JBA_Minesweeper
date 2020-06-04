package minesweeper

import kotlin.random.Random
import minesweeper.MoveResult.*

// filed items
private val UNEXPLORED = 0
private val FREE = -1
private val MARK = -2
private val MINE = -3
private val HINT_RANGE = 1..8

private lateinit var gridSizeRange: IntRange
private lateinit var initialField: Array<Array<Int>>
private lateinit var mutableField: Array<Array<Int>>
private var numberOfMines = -1
private var gridSizeF = -1
private var initPerformed = false
private var initialFieldCreated = false
private var gameOver = false

fun initAndGetGameField(gridSize: Int, numOfMines: Int): Array<Array<Int>> {
    if (gridSize < 2) throw IllegalArgumentException("gridSize must be >= 2")
    if (numOfMines < 1 || numOfMines >= (gridSize * gridSize)) {
        throw IllegalArgumentException("numOfMines must be > 0 and less than gridSize * gridSize")
    }

    gridSizeRange = 0 until gridSize
    gridSizeF = gridSize
    numberOfMines = numOfMines

    mutableField = createAndFillMatrix(gridSize, UNEXPLORED)

    initPerformed = true
    initialFieldCreated = false
    gameOver = false

    return mutableField
}

fun makeMove(x: Int, y: Int, command: Int): MoveResult {
    // input check
    if (!initPerformed) throw IllegalStateException("Call initAndGetGameField first")
    if (gameOver) throw IllegalStateException("The game is over, to start again call initAndGetGameField again")
    if (x !in gridSizeRange || y !in gridSizeRange) throw IllegalArgumentException("Incorrect coords")
    if (command !in 1..2) throw IllegalArgumentException("Incorrect command")
    // init initial game field
    if (!initialFieldCreated) {
        if (command == 1) { // command "free"
            initialField = generateFieldWhereSpecifiedCellIsNotMine(x, y)
        } else { // command "mine"
            initialField = createAndFillMatrix(gridSizeF, FREE)
            addMinesRandPlaces(initialField, numberOfMines)
            addHintsOnField(initialField)
        }
        initialFieldCreated = true
    }
    // make move
    when (command) {
        2 -> { // mine
            mutableField[y][x] = when (mutableField[y][x]) {
                UNEXPLORED -> MARK
                MARK -> UNEXPLORED
                FREE -> return ATTEMPT_TO_MARK_FREE
                in HINT_RANGE -> return ATTEMPT_TO_MARK_HINT
                else -> throw RuntimeException("Something went wrong")
            }
        }
        1 -> { // free
            when (initialField[y][x]) {
                in HINT_RANGE -> {
                    if (mutableField[y][x] in HINT_RANGE) return ATTEMPT_TO_UNCOVER_HINT_AGAIN

                    mutableField[y][x] = initialField[y][x]
                }
                MINE -> {
                    gameOver = true
                    return LOSE
                }
                FREE -> {
                    if (mutableField[y][x] == FREE) return ATTEMPT_TO_UNCOVER_FREE_AGAIN

                    uncoverFreeAreaRecursively(initialField, mutableField, y, x)
                    uncoverHintsAroundFreeArea(initialField, mutableField)
                }
                else -> throw RuntimeException("Incorrect fieldItem found")
            }
        }
        else -> throw RuntimeException("Something went wrong")
    }
    // check if win
    if (isAllMinesMarked(initialField, mutableField) || isAllUncoveredIgnoreMines(initialField, mutableField)) {
        gameOver = true
        return WIN
    }

    return MARKED_UNMARKED
}


private fun generateFieldWhereSpecifiedCellIsNotMine(x: Int, y: Int): Array<Array<Int>> {
    var field: Array<Array<Int>>
    do { // can be optimized
        field = createAndFillMatrix(gridSizeF, FREE)
        addMinesRandPlaces(field, numberOfMines)
    } while (field[y][x] == MINE)

    addHintsOnField(field)
    return field
}

private fun uncoverFreeAreaRecursively(initialField: Array<Array<Int>>, mutableField: Array<Array<Int>>, x: Int, y: Int) {
    mutableField[x][y] = initialField[x][y]
    // top
    if (x - 1 != -1 && mutableField[x - 1][y] != FREE && initialField[x - 1][y] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x - 1, y)
    }
    // top right
    if (x - 1 != -1 && y + 1 != initialField[0].size && mutableField[x - 1][y + 1] != FREE && initialField[x - 1][y + 1] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x - 1, y + 1)
    }
    // right
    if (y + 1 != initialField[0].size && mutableField[x][y + 1] != FREE && initialField[x][y + 1] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x, y + 1)
    }
    // bottom right
    if (x + 1 != initialField.size && y + 1 != initialField[0].size && mutableField[x + 1][y + 1] != FREE && initialField[x + 1][y + 1] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x + 1, y + 1)
    }
    // bottom
    if (x + 1 != initialField.size && mutableField[x + 1][y] != FREE && initialField[x + 1][y] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x + 1, y)
    }
    // left bottom
    if (x + 1 != initialField.size && y - 1 != -1 && mutableField[x + 1][y - 1] != FREE && initialField[x + 1][y - 1] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x + 1, y - 1)
    }
    // left
    if (y - 1 != -1 && mutableField[x][y - 1] != FREE && initialField[x][y - 1] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x, y - 1)
    }
    // left top
    if (x - 1 != -1 && y - 1 != -1 && mutableField[x - 1][y - 1] != FREE && initialField[x - 1][y - 1] == FREE) {
        uncoverFreeAreaRecursively(initialField, mutableField, x - 1, y - 1)
    }
}

private fun uncoverHintsAroundFreeArea(initialField: Array<Array<Int>>, mutableField: Array<Array<Int>>) {
    for (i in mutableField.indices) {
        for (j in mutableField[i].indices) {
            if (initialField[i][j] in HINT_RANGE) {
                if (calculateNumOfItemsAroundItem(mutableField, FREE, i, j) > 0) {
                    mutableField[i][j] = initialField[i][j]
                }
            }
        }
    }
}

private fun isAllMinesMarked(initialField: Array<Array<Int>>, mutableField: Array<Array<Int>>): Boolean {
    for (i in initialField.indices) {
        for (j in initialField[i].indices) {
            if (initialField[i][j] == MINE && mutableField[i][j] != MARK || mutableField[i][j] == MARK && initialField[i][j] != MINE) {
                return false
            }
        }
    }
    return true
}

private fun isAllUncoveredIgnoreMines(a: Array<Array<Int>>, b: Array<Array<Int>>): Boolean {
    for (i in a.indices) {
        for (j in a[i].indices) {
            if (a[i][j] == MINE) continue
            if (a[i][j] != b[i][j]) return false
        }
    }
    return true
}

private fun addMinesRandPlaces(field: Array<Array<Int>>, numOfMines: Int) {
    val coords = getCoordsOfEmptySpots(field)
    if (numOfMines > coords.size) throw IllegalArgumentException("Not enough space for $numOfMines mines")
    for (i in 0 until numOfMines) {
        val newMineCoords = coords.removeAt(Random.nextInt(coords.size))
        addItemOnField(field, MINE, newMineCoords.first, newMineCoords.second)
    }
}

private fun calculateNumOfItemsAroundItem(field: Array<Array<Int>>, fItemToSeek: Int, i: Int, j: Int): Int {
    val rows = field.size
    val cols = field[0].size
    var count = 0

    if (i - 1 != -1) { // top
        count += isEqual(field[i - 1][j], fItemToSeek)
    }
    if (i - 1 != -1 && j + 1 != cols) { // top right
        count += isEqual(field[i - 1][j + 1], fItemToSeek)
    }
    if (j + 1 != cols) { // right
        count += isEqual(field[i][j + 1], fItemToSeek)
    }
    if (i + 1 != rows && j + 1 != cols) {  // bottom right
        count += isEqual(field[i + 1][j + 1], fItemToSeek)
    }
    if (i + 1 != rows) { // bottom
        count += isEqual(field[i + 1][j], fItemToSeek)
    }
    if (i + 1 != rows && j - 1 != -1) { // left bottom
        count += isEqual(field[i + 1][j - 1], fItemToSeek)
    }
    if (j - 1 != -1) { // left
        count += isEqual(field[i][j - 1], fItemToSeek)
    }
    if (i - 1 != -1 && j - 1 != -1) { // left top
        count += isEqual(field[i - 1][j - 1], fItemToSeek)
    }
    return count
}

private fun addHintsOnField(field: Array<Array<Int>>) {
    for (i in field.indices) {
        for (j in field[i].indices) {
            if (field[i][j] != FREE) continue

            val numOfMines = calculateNumOfItemsAroundItem(field, MINE, i, j)
            if (numOfMines > 0) addItemOnField(field, numOfMines, i, j) // add hint
        }
    }
}

private fun addItemOnField(field: Array<Array<Int>>, filedItem: Int, coordRow: Int, coordCol: Int) {
    field[coordRow][coordCol] = filedItem
}

private fun getCoordsOfEmptySpots(field: Array<Array<Int>>): MutableList<Pair<Int, Int>> {
    val coords = mutableListOf<Pair<Int, Int>>()
    field.forEachIndexed { i, val_i -> val_i.forEachIndexed { j, val_ij -> if (val_ij == FREE) coords.add(Pair(i, j)) } }
    return coords
}

private fun isEqual(item1: Int, item2: Int): Int = if (item1 == item2) 1 else 0

private fun replaceMinesWithMarks(field: Array<Array<Int>>): Array<Array<Int>> {
    return field.map { row -> row.map { fieldItem -> if (fieldItem == MINE) MARK else fieldItem }.toTypedArray() }.toTypedArray()
}
