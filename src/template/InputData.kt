package template

import readInputText

sealed class InputData<T: Any>(private val fileName: String){
    class Test<T: Any>(
            fileName: String,
            val checkValue1: T,
            val checkValue2: T? = null,
    ): InputData<T>(fileName)

    class Quest<T: Any>(
            fileName: String
    ): InputData<T>(fileName)

    fun readText(): String {
        return readInputText(fileName)
    }
}