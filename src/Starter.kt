fun main(){
    val day = Day08()

    val part1Test = day.getPart1Result("Day08_test")
    val part2Test = day.getPart2Result("Day08_test")
    println(part1Test)
    check(part1Test == 21)
    check(part2Test == 8)

    val part1 = day.getPart1Result("Day08")
    val part2 = day.getPart2Result("Day08")
    println(part1)
    println(part2)

}