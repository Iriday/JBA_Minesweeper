package minesweeper

fun main() {
    val gameField = initAndGetGameField(9, getNumOfMinesFromConsole(9))

    // main loop
    do {
        val playerMove = getNextMoveFromConsole(gameField)
        val moveResult = makeMove(playerMove[0], playerMove[1], playerMove[2])
        printMoveOutcome(moveResult)
    } while (moveResult != MoveResult.WIN && moveResult != MoveResult.LOSE)
}
