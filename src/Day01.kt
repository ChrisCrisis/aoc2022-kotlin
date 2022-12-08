import java.io.File

fun main() {
    fun parseInputToData(path:String) = File(path).readText()
            .split("\n\n")
            .map {bucket ->
                bucket.split("\n").sumOf { it.toInt() }
            }.sortedDescending()

    fun part1(input: List<Int>): Int {
        return input.first()
    }

    fun part2(input: List<Int>): Int {
        return input.take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = parseInputToData("src/Day01_test.txt")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = parseInputToData("src/Day01.txt")
    println(part1(input))
    println(part2(input))
}
