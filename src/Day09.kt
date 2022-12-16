import template.DailyChallenge
import java.lang.IllegalStateException
import kotlin.math.abs


data class Node<T>(var value: T, val next: Node<T>? = null) {
    override fun toString(): String {
        return if (next != null) {
            "$value -> $next"
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
    private fun Node<Position>.moveNodeTo(myNewPosition: Position): Node<Position>{
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

    private fun moveBy(move: Movement): Rope {
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

class Day09 (inputData: template.InputData<Int>): DailyChallenge<List<Movement>,Int>(inputData){

    override fun parseInput(data: String): List<Movement> {
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

    override fun part1(input: List<Movement>): Int {
        val rope = Rope.createOfLength(2).applyMoves(input)
        return rope.getPositionsVisitedByTail().size
    }

    override fun part2(input: List<Movement>): Int {

        val rope = Rope.createOfLength(10).applyMoves(input)
        return rope.getPositionsVisitedByTail().size
    }
}