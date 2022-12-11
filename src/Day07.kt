import java.lang.IllegalStateException

const val MAX_DIR_SIZE = 100000
const val MIN_FREE_SPACE = 30000000
const val FILE_SYSTEM_TOTAL = 70000000

class FileTree{

    val rootNode: Node = Node("/", null)

    fun findDirectoriesWhere(condition: (Node) -> Boolean): List<Node> {
        fun Node.getSmallerDirsRec(): List<Node>{
            return this.getSubdirs().flatMap {
                if (condition.invoke(it)){
                    listOf(it) + it.getSmallerDirsRec()
                } else {
                    it.getSmallerDirsRec()
                }
            }
        }
        return rootNode.getSmallerDirsRec()
    }

    data class Node constructor(
            val name: String,
            val parent: Node?,
            val children: MutableMap<String, Node>,
            private val fileSize: Int = -1
    ){
        val isDirectory = fileSize == -1

        fun getSize(): Int{
                return if(fileSize == -1)
                    children.values.sumOf { it.getSize() }
                else
                    fileSize
            }

        fun getSubdirs() = children.values.filter { it.isDirectory }

        override fun toString(): String {
            val type = when(fileSize){
                -1 -> "dir"
                else -> "file"
            }
            return "$name ($type, size=$fileSize)"
        }

        constructor(name: String, parent: Node, fileSize: Int): this(name, parent, mutableMapOf(), fileSize)
        constructor(name: String, parent: Node?, children: MutableMap<String, Node> = mutableMapOf()): this(name, parent, children, -1)
    }
}
fun main() {
    fun FileTree.Node.createChildren(data: List<String>){
        if(this.children.isNotEmpty()) throw IllegalStateException()

        data.forEach {
            val split = it.split(" ")
            val nodeName = split[1]
            children[nodeName] = when(split[0]){
                "dir" -> FileTree.Node(nodeName, this)
                else -> FileTree.Node(nodeName, this, split[0].toInt())
            }
        }
    }

    fun parseData(data: String): FileTree {
        val commands = data.split("\n$ ")
        val fileTree = FileTree()

        var currentPositionInTree = fileTree.rootNode

        commands.forEach {
            when{
                it.startsWith("ls") -> {
                    val listData = it.split("\n")
                    currentPositionInTree.createChildren(listData.subList(1,listData.size))
                }
                it.startsWith("cd") -> {
                    it.removePrefix("cd ").let {
                        currentPositionInTree = when(it){
                            "/" -> fileTree.rootNode
                            ".." -> currentPositionInTree.parent!!
                            else -> currentPositionInTree.children[it]!!

                        }
                    }
                }
            }
        }

        return fileTree
    }

    fun part1(data: FileTree): Int {
        return data.findDirectoriesWhere{ it.getSize() < MAX_DIR_SIZE }
                .sumOf { it.getSize() }
    }

    fun part2(data: FileTree): Int {
        val usedSize = data.rootNode.getSize()
        val spaceToFreeUp = MIN_FREE_SPACE + usedSize - FILE_SYSTEM_TOTAL

        return data.findDirectoriesWhere{ it.getSize() >= spaceToFreeUp }
                .minOf { it.getSize() }
    }

    val testInput = readInputText("Day07_test")
    val testTree = parseData(testInput)
    println(part1(testTree))
    println(part2(testTree))
    check(testTree.rootNode.getSize() == 48381165)
    check(part1(testTree) == 95437)
    check(part2(testTree) == 24933642)

    val input = readInputText("Day07")
    val fileTree = parseData(input)
    println(part1(fileTree))
    println(part2(fileTree))
}