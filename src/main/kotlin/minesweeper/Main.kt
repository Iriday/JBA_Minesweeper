package minesweeper

fun main() {
    val gameField = initAndGetGameField(9, getNumOfMinesFromConsole())

    // main loop
    do {
        val playerMove = getNextMoveFromConsole(gameField)
        val moveResult = makeMove(playerMove.x, playerMove.y, playerMove.moveOption)
        printMoveOutcome(moveResult)
    } while (moveResult != MoveResult.WIN && moveResult != MoveResult.LOSE)
}
