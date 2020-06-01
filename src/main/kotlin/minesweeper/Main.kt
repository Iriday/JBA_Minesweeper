package minesweeper

fun main() {
    val filed10x10 = generateSafeField(10)
    addMinesRandPlaces(filed10x10, getNumOfMinesFromConsole())
    addHintsOnField(filed10x10)
    formatAndPrintFiled(filed10x10)
}
