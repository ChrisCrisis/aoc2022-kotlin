
fun main() {

    fun parseData(data: String): String {
        return TODO()
    }

    fun part1(data: String): Int {
        return TODO()
    }

    fun part2(data: String): Int {
        return TODO()
    }

    val testInput = readInputText("DayX_test")
    val testTree = parseData(testInput)
    println(part1(testTree))
    check(part1(testTree) == 21)
//    check(part2(testTree) == 24933642)

    val input = readInputText("DayX")
    val fileTree = parseData(input)
    println(part1(fileTree))
//    println(part2(fileTree))
}