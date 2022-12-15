import template.InputData

fun main(){
    val inputFileBaseName = "Day08"
    val inputData = listOf(
            InputData.Test("${inputFileBaseName}_test", 21, 8),
    )

    val ignorePart2 = inputData.any { it.checkValue2 == null }
    inputData.all {
        Day08(it).evaluate(!ignorePart2)
    }

    Day08(InputData.Quest(inputFileBaseName)).evaluate(!ignorePart2)
}