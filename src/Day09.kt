import template.DailyChallenge
import kotlin.IllegalStateException
import kotlin.math.abs
import kotlin.math.max


data class Node<T>(var value: T, val next: Node<T>? = null) {
    override fun toString(): String {
        return if (next != null) {
            "$value -> $next"
        } else {
            "$value"
        }
    }
    fun isTail(): Boolean {
        return this.next == null
    }
}
sealed class Movement(
        val distance: Int
) {

    class Up(distance: Int) : Movement(distance)
    class Down(distance: Int) : Movement(distance)
    class Left(distance: Int) : Movement(distance)
    class Right(distance: Int) : Movement(distance)

    override fun toString(): String {
        return "Movement(direction=${this::class.simpleName}, distance=$distance)"
    }
}

data class Position(
        val x: Int = 0,
        val y: Int = 0,
){
    fun isTouching(position: Position): Boolean{
        return abs(this.x - position.x) <= 1 && abs(this.y - position.y) <= 1
    }

    fun isInLineWith(position: Position): Boolean {
        return ( abs(this.x - position.x) == 0 ) xor ( abs(this.y - position.y) == 0 )
    }
    fun isAdjacentTo(position: Position): Boolean{
        return ( abs(this.x - position.x) == 1 ) xor ( abs(this.y - position.y) == 1 )
    }

    fun isDiagonallyTo(position: Position): Boolean{
        return abs(this.x - position.x) == 1 && abs(this.y - position.y) == 1
    }

    fun getDistanceTo(target: Position): Int {
        return abs(this.x - target.x) + abs(this.y - target.y)
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

    operator fun minus(value: Position) = Position(
        this.x - value.x,
        this.y - value.y
    )

    operator fun plus(value: Position) = Position(
        this.x + value.x,
        this.y + value.y
    )

    operator fun div(divider: Int): Position {
        return Position(this.x / divider, this.y / divider)
    }
}

data class Rope(
        val head: Node<Position>,
        private val visitedPositions: HashSet<Position> = hashSetOf(Position()),
        private val include1: Boolean = true,
        private val include2: Boolean = false
){
    private fun Node<Position>.moveNodeTo(myNewPosition: Position): Node<Position>{
        check(this.value.isTouching(myNewPosition))

        val updatedPosNode = this.copy(value = myNewPosition)
        return when {
            this.isTail() -> {
                visitedPositions.add(myNewPosition)
                updatedPosNode
            }

            myNewPosition.isTouching(next!!.value) -> updatedPosNode
            else -> {
                val nextNewPos = when{
                    myNewPosition.isInLineWith(next.value) && !myNewPosition.isTouching(next.value) -> {
                        check(
                                (myNewPosition.x == next.value.x  || myNewPosition.y == next.value.y) &&
                                    myNewPosition.getDistanceTo(next.value) == 2
                        )
                        val diff = myNewPosition - next.value
                        (next.value + (diff / 2))
                    }
                    else -> {
                        check(
                                myNewPosition.x != next.value.x &&
                                myNewPosition.y != next.value.y &&
                                !myNewPosition.isTouching(next.value)
                        )
                        val myDiff = (myNewPosition - next.value).let {
                            Position(it.x.coerceIn(-1..1), it.y.coerceIn(-1..1), )
                        }
                        check(myDiff.getDistanceTo(Position()) == 2)

                        val appliedDiff = (next.value + myDiff)


                        val result = appliedDiff
                        result.also {
                            try {
                                check(it.isDiagonallyTo(next.value))
                            } catch (e: IllegalStateException){
                                throw IllegalArgumentException("Failed to verify the new $it is diagonal to the old ${next.value}")
                            }
                        }
                    }
                }.also {
                    check(it.isTouching(myNewPosition) && it.isTouching(next.value))
                }
                updatedPosNode.copy(next = this.next.moveNodeTo(nextNewPos))
            }
        }






//        return when {
//            this.isTail() -> {
//                visitedPositions.add(myNewPosition)
//                updatedPosNode
//            }
//            myNewPosition.isTouching(next!!.value) -> updatedPosNode
//            next.value.isAdjacentTo(this.value) ->
//                updatedPosNode.copy(
//                    next = this.next.moveNodeTo(this.value)
//                )
//            myNewPosition.isInLineWith(next.value)
//                && !myNewPosition.isTouching(next.value) ->
//            {
//                val diff = myNewPosition - next.value
//                val newNextPosition = next.value + (diff / 2)
//                updatedPosNode.copy(
//                        next = this.next.moveNodeTo(newNextPosition)
//                )
//            }
//            else -> {
//                val myDiff = myNewPosition - this.value
//                val nextNewPos = next.value + myDiff
//                updatedPosNode.copy(
//                        next = this.next.moveNodeTo(nextNewPos)
//                )
//            }
//        }
    }

    fun getPositionsVisitedByTail(): List<Position> {
        return visitedPositions.toList()
    }

    fun applyMoves(moves: List<Movement>) : Rope {
        println()
        return moves.fold(this){ rope, move ->
            rope.moveBy(move).apply {
                //println("Rope after move('$move') was:\n$this\n")
            }
        }
    }

    private fun moveBy(move: Movement): Rope {
        var newHead = this.head
        (0 until move.distance).forEach { _ ->
            val headPosAfterMove = newHead.value.calcPositionAfter(move)
            newHead = newHead.moveNodeTo(headPosAfterMove).also {
//                this.copy(head = it).printVisited(includeRope = true)
            }
        }
        return this.copy(head = newHead).apply {
//            printVisited(includeRope = true)
        }
    }

    fun printVisited(includeRope: Boolean = true){
        fun Rope.getPositions(): HashSet<Position> {
            val allPositions = hashSetOf<Position>()
            var node: Node<Position>? = head
            while (node != null){
                allPositions.add(node.value)
                node = node.next
            }
            return allPositions
        }
        val ropePositions = getPositions()
        val allRelevantPositions = visitedPositions + ropePositions
        val maxSize = allRelevantPositions.maxOf { max( abs(it.x), abs(it.y)) }
        val verticalRange = (allRelevantPositions.maxOf { it.y } downTo allRelevantPositions.minOf { it.y }) //(maxSize downTo -maxSize)
        val horizontalRange = (allRelevantPositions.minOf { it.x } .. allRelevantPositions.maxOf { it.x }) //(-maxSize until maxSize)

        println("Visited positions: ${visitedPositions.size}")
        verticalRange.forEach { y ->
            horizontalRange.forEach { x ->
                val symbol = when{
                    includeRope && x == head.value.x && y == head.value.y-> 'H'
                    includeRope && ropePositions.contains(Position(x,y)) -> '0'
                    x == 0 && y == 0 -> 'S'
                    visitedPositions.contains(Position(x,y)) -> '*'
//                    Position(x,y).isInLineWith(Position()) -> '+'
                    else -> '.'
                }
                print(symbol)
            }
            println(" $y")
        }
        horizontalRange.forEach {
            print("${abs(it)}")
        }
        println()
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

        val rope = Rope.createOfLength(10)
                .copy(include2 = true)
                .applyMoves(input)
//        rope.printVisited()
        return rope.getPositionsVisitedByTail().size
    }
}