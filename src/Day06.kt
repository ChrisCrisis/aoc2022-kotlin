import java.io.File

const val SEARCH_WINDOW_SIZE = 4
const val MESSAGE_MARKER_SIZE = 14
fun main() {
    fun parseInputToData(path: String): String{
        return File(path).readText()
    }

    fun String.hasOnlyUniqueCharacters(): Boolean {
        return this.all {letter ->
            this.count { it == letter } == 1
        }
    }

    fun String.findUniqueCharPacket(packetSize: Int): Int {
        return this.windowedSequence(packetSize).indexOfFirst { window ->
            window.hasOnlyUniqueCharacters()
        } + packetSize

    }

    fun part1(data: String): Int {
        return data.findUniqueCharPacket(SEARCH_WINDOW_SIZE)
    }

    fun part2(data: String): Int {
        return data.findUniqueCharPacket(MESSAGE_MARKER_SIZE)
    }

    val testInput = parseInputToData("src/Day06_test.txt")
    println(part1(testInput))

    val input = parseInputToData("src/Day06.txt")
    println(part1(input))
    println(part2(input))
}