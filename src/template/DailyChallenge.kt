package template

import readInputText

abstract class DailyChallenge <INPUT: Any, RESULT_TYPE:Any> {

    protected abstract fun parseInput(data: String): INPUT
    protected abstract fun part1(input: INPUT): RESULT_TYPE
    protected abstract fun part2(input: INPUT): RESULT_TYPE

    fun getPart1Result(fileName: String): RESULT_TYPE{
        val inputData = readInputText(fileName)
        val parsedInput = this.parseInput(inputData)
        return part1(parsedInput)
    }
    fun getPart2Result(fileName: String): RESULT_TYPE{
        val inputData = readInputText(fileName)
        val parsedInput = this.parseInput(inputData)
        return part2(parsedInput)
    }

    fun check(result: RESULT_TYPE, value: RESULT_TYPE){
        check(result == value)
    }
}