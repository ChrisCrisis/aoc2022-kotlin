import java.lang.IllegalStateException

fun main() {
    fun processInput(data: List<String>): List<Char> {
        return data.map{ bucket ->
            val firstHalf = bucket.substring(0 until bucket.length/2)
            val secondHalf = bucket.substring(bucket.length/2 until bucket.length )

            firstHalf.forEach {
                if(secondHalf.contains(it)){
                    return@map it
                }
            }
            throw IllegalStateException("No match found")
        }
    }

    fun List<String>.processInputChunked(): List<Char> {
        return this.chunked(3) {group ->
            val minion1 = group[1]
            val minion2 = group[2]
            group.first().forEach {
                if(minion1.contains(it) && minion2.contains(it)){
                    return@chunked it
                }
            }
            throw IllegalStateException("No match found")
        }
    }

    fun Char.getScore(): Int {
        return when{
            this.isLowerCase() -> this.code - 96
            this.isUpperCase() -> this.code - 64 + 26
            else -> throw IllegalStateException()
        }
    }

    fun part1(data: List<String>): Int {
        return processInput(data).sumOf { it.getScore() }
    }

    fun part2(data: List<String>): Int {
        return data.processInputChunked().sumOf { it.getScore() }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}