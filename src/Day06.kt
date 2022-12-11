const val SEARCH_WINDOW_SIZE = 4
const val MESSAGE_MARKER_SIZE = 14
fun main() {

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

    val testInput = parseInputText("Day06_test")
    println(part1(testInput))

    val input = parseInputText("Day06")
    println(part1(input))
    println(part2(input))
}