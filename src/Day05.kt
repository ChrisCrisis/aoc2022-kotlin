import java.io.File

data class MovementOperation(
        val from: Int,
        val to: Int,
        val amount: Int,
)

data class StorageSlot(
        //val slotId: Int,
        val currentContainer: List<Char>,
)
data class InputData(
        val craneSlots: List<StorageSlot>,
        val moves: List<MovementOperation>,
)

const val CRANE_START_OFFSET = 1
const val CRANE_MOVE_OFFSET = 4

fun <E> List<List<E>>.transpose(): List<List<E>> {
    val result : List<MutableList<E>> = List(this.maxOf { it.size }){
        mutableListOf()
    }
    this.forEach {
        it.forEachIndexed { index, e ->
            result[index].add(e)
        }
    }
    return result
}
fun main() {
    fun parseCraneSlots(craneData: List<String>): List<StorageSlot>{
        val cleanData = craneData.take(craneData.size - 1)
        val some = cleanData.reversed().map {
            it.substring(CRANE_START_OFFSET,it.length-1)
                    .slice(it.indices step CRANE_MOVE_OFFSET)
                    .toList()
        }.transpose().map { containerInfo ->
            StorageSlot(containerInfo.filter { it != ' ' })
        }
        return some
    }

    fun parseMovementOperations(operations: List<String>): List<MovementOperation> {
        return operations.map {
            val split = it.split(" ")
            MovementOperation(
                    split[3].toInt(),
                    split[5].toInt(),
                    split[1].toInt(),
            )
        }
    }

    fun parseInputToData(path: String): InputData{
        val sections = File(path).readText()
                .split("\n\n")
        val craneSlots = parseCraneSlots(sections[0].split("\n"))
        val moves = parseMovementOperations(sections[1].split("\n"))

        return InputData(
                craneSlots,
                moves,
        )
    }

    fun getLastItemFromCranes(craneSlots: List<StorageSlot>): String {
        return craneSlots.map {
            it.currentContainer.last()
        }.joinToString("")
    }

    fun InputData.getCraneSlotsAfterMoves9000(): List<StorageSlot>{
        val initData = this.craneSlots.map { it.currentContainer.toMutableList() }
        this.moves.forEach { move ->
            val from = initData[move.from-1]
            val to = initData[move.to-1]

            from.takeLast(move.amount).reversed().forEach {
                to.add(it)
                from.removeLast()
            }
        }

        return initData.map {
            StorageSlot(it)
        }
    }

    fun InputData.getCraneSlotsAfterMoves9001(): List<StorageSlot> {
        val initData = this.craneSlots.map { it.currentContainer.toMutableList() }
        this.moves.forEach { move ->
            val from = initData[move.from - 1]
            val to = initData[move.to - 1]

            from.takeLast(move.amount).forEach {
                to.add(it)
                from.removeLast()
            }
        }

        return initData.map {
            StorageSlot(it)
        }
    }

    fun part1(data: InputData): String {
        val slotsAfterMoves = data.getCraneSlotsAfterMoves9000()
        return getLastItemFromCranes(slotsAfterMoves)
    }

    fun part2(data: InputData): String {
        val slotsAfterMoves = data.getCraneSlotsAfterMoves9001()
        return getLastItemFromCranes(slotsAfterMoves)
    }

    val testInput = parseInputToData("src/Day05_test.txt")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = parseInputToData("src/Day05.txt")
    println(part1(input))
    println(part2(input))
}