
fun main() {
    fun String.getSectionRange(): IntRange {
        return this.split("-").let {
            it.first().toInt() .. it.last().toInt()
        }
    }

    fun List<String>.processInput(): List<ElvenPair>{
        return this.map {
            val elves = it.split(",")
            ElvenPair(
                    elves[0].getSectionRange(),
                    elves[1].getSectionRange(),
            )
        }
    }

    fun part1(data: List<String>): Int {
        return data.processInput().filter { pair ->
            pair.firstElfSections.all {
                pair.secondElfSections.contains(it)
            } or pair.secondElfSections.all {
                pair.firstElfSections.contains(it)
            }
        }.size
    }

    fun part2(data: List<String>): Int {
        return data.processInput().filter { pair ->
            pair.firstElfSections.any {
                pair.secondElfSections.contains(it)
            } or pair.secondElfSections.any {
                pair.firstElfSections.contains(it)
            }
        }.size
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class ElvenPair(
        val firstElfSections: IntRange,
        val secondElfSections: IntRange,
)