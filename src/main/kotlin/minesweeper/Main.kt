package minesweeper

fun main() {
    val gameField = initAndGetGameField(9, getNumOfMinesFromConsole())

    // main loop
    do {
        val (x, y) = getNextMoveCoordsFromConsole(gameField)
        val moveResult = makeMove(x, y)
        printMoveOutcome(moveResult)
    } while (moveResult != MoveResult.WIN)
}
