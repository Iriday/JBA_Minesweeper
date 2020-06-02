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

fun formatFiled(field: Array<Array<Int>>): MutableList<String> {
    return field.map { row -> row.joinToString("").replace("0", ".").replace("-1", "X") }.toMutableList()
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
