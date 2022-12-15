import template.DailyChallenge

class TreeGrid(
    private val trees: Array<Array<Tree>>
){
    private val sizeX by lazy { trees.first().size }
    private val sizeY by lazy { trees.size }

    fun getTreesVisibleFromOutside(): Int {
        return trees.sumOf { treeLine ->
            treeLine.count { tree ->
                tree.isVisible()
            }
        }
    }

    fun getHighestScore(): Int {
        return trees.maxOf {treeLine ->
            treeLine.maxOf {
                it.getScore()
            }
        }
    }

    class Tree(
            val x: Int,
            val y: Int,
            val height: Int,
    )

    //region Extensions
    private fun Tree.getScore(): Int {
        val horizontalScore = getTreesInRow(this.y).getLineScore(this)
        val verticalScore = getTreesInColumn(this.x).getLineScore(this)
        return horizontalScore * verticalScore
    }

    private fun Tree.isVisible(): Boolean{
        return this.isOnTheEdge()
                || this.isVerticallyVisible()
                || this.isHorizontallyVisible()
    }

    private fun Tree.isOnTheEdge(): Boolean =
            this.x == 0 || this.x == sizeX-1 ||
            this.y == 0 || this.y == sizeY-1
    private fun Tree.isVerticallyVisible(): Boolean {
        return getTreesInColumn(this.x).isTreeVisible(this)

    }

    private fun Tree.isHorizontallyVisible(): Boolean {
        return getTreesInRow(this.y).isTreeVisible(this)
    }
    //endregion

    private fun getTreesInColumn(index: Int): List<Tree>{
        return trees.map { it[index] }
    }

    private fun getTreesInRow(index: Int): List<Tree>{
        return trees[index].toList()
    }

    private fun List<Tree>.getSurroundingSubListsFor(tree: Tree): List<List<Tree>>{
        val treeIndex = this.indexOf(tree)
        return listOf(
                this.subList(0, treeIndex).reversed(),
                this.subList(treeIndex + 1, this.size),
        )
    }
    private fun List<Tree>.isTreeVisible(tree: Tree): Boolean {
        val surroundingSides = getSurroundingSubListsFor(tree)
        return surroundingSides.any {treeLine ->
            treeLine.maxOf { it.height } < tree.height
        }
    }

    private fun List<Tree>.getLineScore(tree: Tree): Int {
        val surroundingSides = getSurroundingSubListsFor(tree)
        return surroundingSides.map {treeLine ->
            val highTreeIndex = treeLine.indexOfFirst { it.height >= tree.height } + 1
            if(highTreeIndex == 0) treeLine.size else highTreeIndex
        }.fold(1) { first, second ->
            first * second
        }
    }
}

class Day08 : DailyChallenge<TreeGrid,Int>(){
    override fun parseInput(data: String): TreeGrid {
        val lines = data.split("\n")
        val lineArray = lines.mapIndexed { y, line ->
            line.mapIndexed { x, treeHeight ->
                TreeGrid.Tree(
                        x,
                        y,
                        Character.getNumericValue(treeHeight)
                )
            }.toTypedArray()
        }.toTypedArray()
        return TreeGrid(lineArray)
    }

    override fun part1(input: TreeGrid): Int {
        return input.getTreesVisibleFromOutside()
    }

    override fun part2(input: TreeGrid): Int {
        return input.getHighestScore()
    }
}