package minesweeper

fun main() {
    val field = generateSafeField(9)
    addMinesRandPlaces(field, getNumOfMinesFromConsole())
    addHintsOnField(field)
    printFiled(addGridCoordinates(formatFiled(field)))
}
