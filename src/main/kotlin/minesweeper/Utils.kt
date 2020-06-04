package minesweeper

fun deepContentEquals(a: Array<Array<Int>>, b: Array<Array<Int>>): Boolean {
    if (a.size != b.size) return false
    for (i in a.indices) {
        if (!a[i].contentEquals(b[i])) return false
    }
    return true
}

fun deepArrayCopy(matrix: Array<Array<Int>>): Array<Array<Int>> {
    return matrix.map { row -> row.map { it }.toTypedArray() }.toTypedArray()
}

fun createAndFillMatrix(size: Int, item: Int): Array<Array<Int>> {
    return Array(size) { Array(size) { item } }
}

fun <T> copyItemFromMatrixAToMatrixB(a: Array<Array<T>>, b: Array<Array<T>>, itemToCopy: T) {
    for (i in a.indices) {
        for (j in a[i].indices) {
            if (a[i][j] == itemToCopy) b[i][j] = a[i][j]
        }
    }
}
