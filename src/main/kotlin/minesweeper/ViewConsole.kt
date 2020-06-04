package minesweeper

import kotlin.NumberFormatException
import minesweeper.MoveResult.*
import java.lang.RuntimeException

fun getNextMoveFromConsole(gameField: Array<Array<Int>>): List<Int> {
    while (true) {
        printFiled(addGridCoordinates(formatFiled(gameField)))
        print("Set/unset mines marks or claim a cell as free: ")
        val input = readLine()!!.trim().toUpperCase().split(" ")
        val output: List<Int>

        try {
            val command =
                    if (input[2].toLowerCase() == "free") {
                        1
                    } else if (input[2].toLowerCase() == "mine") {
                        2
                    } else {
                        println("Incorrect input, please try again.")
                        continue
                    }
            output = listOf(input[0].toInt() - 1, input[1].toInt() - 1, command) // grid numbers to indexes
        } catch (e: RuntimeException) {
            println("Incorrect input, please try again.")
            continue
        }

        if (input.size != 3 || output[0] !in gameField.indices || output[1] !in gameField.indices) {
            println("Incorrect input, please try again.")
        } else {
            println()
            return output
        }
    }
}

fun printMoveOutcome(moveResult: MoveResult) {
    when (moveResult) {
        WIN -> println("Congratulations! You found all the mines!")
        LOSE -> println("You stepped on a mine and failed!")
        ATTEMPT_TO_UNCOVER_HINT_AGAIN -> println("There is a number here!")
        ATTEMPT_TO_UNCOVER_FREE_AGAIN -> println("You already explored this area!")
        ATTEMPT_TO_MARK_FREE, ATTEMPT_TO_MARK_HINT -> println("This area is already explored, mine can't be here!")
        MARKED_UNMARKED -> Unit
    }
}

fun getNumOfMinesFromConsole(): Int {
    print("How many mines do you want on the filed? ")
    var numOfMines: Int
    while (true) {
        try {
            numOfMines = readLine()!!.trim().toInt()
            if (numOfMines < 0) {
                println("Incorrect input, please try again.")
            } else {
                return numOfMines
            }
        } catch (e: NumberFormatException) {
            println("Incorrect input, please try again.")
        }
    }
}

fun formatFiled(field: Array<Array<Int>>): MutableList<String> {
    return field.map { row ->
        row.joinToString("")
                .replace("0", ".")
                .replace("-1", "/")
                .replace("-2", "*")
                .replace("-3", "X")
    }.toMutableList()
}

fun addGridCoordinates(field: MutableList<String>): MutableList<String> {
    val numbersTop = StringBuilder()
    for (i in 1..field[0].length) numbersTop.append(i)

    field.add(0, "-".repeat(field[0].length))
    field.add(0, numbersTop.toString())
    field.add(field[1])

    val numOfRows = field.size - 3
    val numOfRowsLen = numOfRows.toString().length
    var rowCounter = 0
    for (i in 2..field.lastIndex - 1) {
        field[i] = String.format("%${numOfRowsLen}d|${field[i]}|", ++rowCounter)
    }

    field[0] = "${" ".repeat(numOfRowsLen)}|${field[0]}|"
    field[1] = "${"-".repeat(numOfRowsLen)}|${field[1]}|"
    field[field.lastIndex] = field[1]

    return field
}

fun printFiled(field: List<String>) = field.forEach { println(it) }
