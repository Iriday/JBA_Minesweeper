package minesweeper

import java.lang.NumberFormatException


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

fun formatAndPrintFiled(field: Array<Array<Int>>) {
    field.forEach { row -> println(row.joinToString("").replace("0", ".").replace("-1", "X")) }
}
