package minesweeper

fun main() {
    while (showMainMenuAndGetOption() == 1) {
        val gameField = initAndGetGameField(9, getNumOfMinesFromConsole(9))
        printFiled(addGridCoordinates(formatFiled(gameField)))

        do {
            val playerMove = getNextMoveFromConsole(gameField)
            val moveResult = makeMove(playerMove[0], playerMove[1], playerMove[2])
            printFiled(addGridCoordinates(formatFiled(gameField)))
            printMoveOutcome(moveResult)
        } while (moveResult != MoveResult.WIN && moveResult != MoveResult.LOSE)
    }
}
