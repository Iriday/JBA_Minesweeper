package minesweeper

fun main() {
    val filed10x10 = generateSafeField(10)
    addMinesRandPlaces(filed10x10, getNumOfMinesFromConsole())
    printField(filed10x10)
}
