import java.lang.IllegalStateException

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            it.split(" ")
        }.map { (opo, pl) ->
            val opponentChoice = Choice.fromOpponent(opo)
            GameMove(
                    opponentChoice,
                    Choice.fromPlayer(pl),
            )
        }.sumOf {
            it.calcPlayerScore()
        }
    }

    fun part2(input: List<String>): Int {
        return input.map {
            it.split(" ")
        }.map { (opo, pl) ->
            val opponentChoice = Choice.fromOpponent(opo)
            GameMove(
                    opponentChoice,
                    Choice.fromCondition(opponentChoice,pl),
            )
        }.sumOf {
            it.calcPlayerScore()
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))

}

data class GameMove(
        val opponentChoice: Choice,
        val playerChoice: Choice,
){
    fun calcPlayerScore(): Int {
        val result = when {
            opponentChoice == playerChoice -> 3
            (opponentChoice == Choice.ROCK && playerChoice == Choice.PAPER) ||
            (opponentChoice == Choice.PAPER && playerChoice == Choice.SCISSOR) ||
            (opponentChoice == Choice.SCISSOR && playerChoice == Choice.ROCK) -> 6
            (opponentChoice == Choice.SCISSOR && playerChoice == Choice.PAPER) ||
            (opponentChoice == Choice.ROCK && playerChoice == Choice.SCISSOR) ||
            (opponentChoice == Choice.PAPER && playerChoice == Choice.ROCK) -> 0
            else -> throw IllegalStateException()
        }

        return playerChoice.expectedPoints + result
    }
}
enum class Choice(
        val expectedPoints: Int
){
    ROCK(1),
    PAPER(2),
    SCISSOR(3);

    companion object{
        fun fromCondition(opponentChoice: Choice, choice: String) = when(choice){
            "X" -> opponentChoice.getChoiceLoss()
            "Y" -> opponentChoice
            "Z" -> opponentChoice.getChoiceWin()
            else -> throw IllegalArgumentException()
        }

        fun fromPlayer(choice: String) = when(choice){
            "X" -> ROCK
            "Y" -> PAPER
            "Z" -> SCISSOR
            else -> throw IllegalArgumentException()
        }

        fun fromOpponent(choice: String) = when(choice){
            "A" -> ROCK
            "B" -> PAPER
            "C" -> SCISSOR
            else -> throw IllegalArgumentException()
        }
    }
}

fun Choice.getChoiceWin() = when(this){
    Choice.ROCK -> Choice.PAPER
    Choice.PAPER -> Choice.SCISSOR
    Choice.SCISSOR -> Choice.ROCK
}

fun Choice.getChoiceLoss() = when(this){
    Choice.ROCK -> Choice.SCISSOR
    Choice.PAPER -> Choice.ROCK
    Choice.SCISSOR -> Choice.PAPER
}