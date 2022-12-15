import java.lang.IllegalStateException
import kotlin.math.abs


data class Node<T>(var value: T, val next: Node<T>? = null) {
    override fun toString(): String {
        return if (next != null) {
            "$value -> ${next.toString()}"
        } else {
            "$value"
        }
    }
}
sealed class Movement(
        val distance: Int
) {

    class Up(distance: Int) : Movement(distance)
    class Down(distance: Int) : Movement(distance)
    class Left(distance: Int) : Movement(distance)
    class Right(distance: Int) : Movement(distance)
}

data class Position(
        val x: Int = 0,
        val y: Int = 0,
){
    fun isAdjacentTo(position: Position): Boolean{
        return abs(this.x - position.x) <= 1 && abs(this.y - position.y) <= 1
    }

    fun calcPositionAfter(move: Movement): Position {
        return when(move){
            is Movement.Up ->
                this.copy( y = this.y + 1 )
            is Movement.Down ->
                this.copy( y = this.y - 1 )
            is Movement.Left ->
                this.copy( x = this.x - 1 )
            is Movement.Right ->
                this.copy( x = this.x + 1)
        }
    }
}

data class Rope(
        val head: Node<Position>,
        private val visitedPositions: HashSet<Position> = hashSetOf(Position()),
){
    fun Node<Position>.moveNodeTo(myNewPosition: Position): Node<Position>{
        val nextPosition = this.next?.let { nextNode ->
            if (!myNewPosition.isAdjacentTo(nextNode.value)) {
                nextNode.moveNodeTo(this.value)
            } else nextNode
        } ?: kotlin.run {
            visitedPositions.add(myNewPosition)
            null
        }
        return Node(myNewPosition, nextPosition)
    }

    fun getPositionsVisitedByTail(): List<Position> {
        return visitedPositions.toList()
    }

    fun applyMoves(moves: List<Movement>) : Rope {
        return moves.fold(this){ rope, move ->
            rope.moveBy(move)
        }
    }

    fun moveBy(move: Movement): Rope {
        var newHead = this.head
        (0 until move.distance).forEach { _ ->
            val headPosAfterMove = newHead.value.calcPositionAfter(move)
            newHead = newHead.moveNodeTo(headPosAfterMove)
        }
        return this.copy(head = newHead)
    }

    companion object {
        fun createOfLength(size: Int): Rope{
            val nodes = List(size-1) { Node(Position()) }
            val head = nodes.foldRight(nodes.last()){ previous, current ->
                previous.copy(next = current)
            }
            return Rope(head)
        }
    }
}
fun main() {

    fun parseData(data: String): List<Movement> {
        return data.split("\n").map {moveData ->
            val split = moveData.split(" ")
            when(split[0]){
                "U" -> Movement.Up(split[1].toInt())
                "D" -> Movement.Down(split[1].toInt())
                "L" -> Movement.Left(split[1].toInt())
                "R" -> Movement.Right(split[1].toInt())
                else -> throw IllegalStateException()
            }
        }
    }

    fun part1(data: List<Movement>): Int {
        val rope = Rope.createOfLength(2).applyMoves(data)
        return rope.getPositionsVisitedByTail().size
    }

    fun part2(data: List<Movement>): Int {

        val rope = Rope.createOfLength(10).applyMoves(data)
        return rope.getPositionsVisitedByTail().size
    }

    val testInput = readInputText("Day09_test")
    val testTree = parseData(testInput)
    println(part1(testTree))
    check(part1(testTree) == 13)

    val testInputP2 = readInputText("Day09_test_p2")
    val testTreeP2 = parseData(testInputP2)
    println(part2(testTreeP2))
    check(part2(testTreeP2) == 36)

    val input = readInputText("Day09")
    val fileTree = parseData(input)
    println(part1(fileTree))
    println(part2(fileTree))
}