package template

abstract class DailyChallenge <INPUT: Any, RESULT_TYPE:Any>(
        private val input: InputData<RESULT_TYPE>
) {
    private val puzzleInput by lazy { input.readText() }

    protected abstract fun parseInput(data: String): INPUT
    protected abstract fun part1(input: INPUT): RESULT_TYPE
    protected abstract fun part2(input: INPUT): RESULT_TYPE

    private fun getPart1Result(): RESULT_TYPE{
        val parsedInput = this.parseInput(puzzleInput)
        return part1(parsedInput)
    }
    private fun getPart2Result(): RESULT_TYPE{
        val parsedInput = this.parseInput(puzzleInput)
        return part2(parsedInput)
    }

    private fun check(result: RESULT_TYPE, value: RESULT_TYPE){
        check(result == value)
    }

    private fun processResult(result: RESULT_TYPE, checkValue: (InputData.Test<RESULT_TYPE>) -> RESULT_TYPE): Boolean {
        return when(this.input){
            is InputData.Test -> {
                try {
                    check(result, checkValue(input))
                    println("TestResult: $result")
                    true
                } catch (e: IllegalStateException){
                    if(e.message == "Check failed."){
                        println("TestResult: $result")
                        println(e.message)
                    }
                    throw e
                }
            }
            is InputData.Quest -> {
                println("Puzzle Result:\n$result")
                true
            }
        }
    }

    private fun processPart1(): Boolean{
        print("---> Part 1 - ")
        val result = getPart1Result()
        return processResult(result){ it.checkValue1 }
    }
    private fun processPart2(): Boolean{
        print("---> Part 2 - ")
        val result = getPart2Result()
        return processResult(result){
            it.checkValue2 ?: throw IgnorePart2Exception()
        }
    }

    fun evaluate(checkPart2: Boolean = true): Boolean{
        val step = input::class.simpleName
        println("\n\n==============>   $step")
        return processPart1() && try {
            if(checkPart2) processPart2() else true
        } catch (e: IgnorePart2Exception){
            true
        }
    }
}
class IgnorePart2Exception: IllegalStateException()