import template.InputData

fun main(){
    val inputFileBaseName = "Day09"
    val inputData = listOf(
            InputData.Test("${inputFileBaseName}_test", 13, 1),
            InputData.Test("${inputFileBaseName}_test_p2", 88, 36),
    )

    val ignorePart2 = inputData.any { it.checkValue2 == null }
    inputData.all {
        Day09(it).evaluate(!ignorePart2)
    }

    Day09(InputData.Quest(inputFileBaseName)).evaluate(!ignorePart2)
}