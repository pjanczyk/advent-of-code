import java.nio.file.{Files, Path}

object day3 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt"))
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    input.linesIterator.map { rucksack =>
      val (compartment1, compartment2) = rucksack.splitAt(rucksack.length / 2)
      val sharedItem = (compartment1 intersect compartment2).head
      itemPriority(sharedItem)
    }.sum
  }

  def part2(input: String): Int = {
    input.linesIterator
      .sliding(size = 3, step = 3)
      .map { group =>
        val badge = group.reduce(_ intersect _).head
        itemPriority(badge)
      }
      .sum
  }

  private def itemPriority(item: Char): Int =
    item match {
      case _ if ('a' to 'z').contains(item) => item - 'a' + 1
      case _ if ('A' to 'Z').contains(item) => item - 'A' + 27
    }
}
