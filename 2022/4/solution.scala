import java.nio.file.{Files, Path}

object day4 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt"))
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    input.linesIterator
      .map { line =>
        val Array(start1, end1, start2, end2) = line.split(Array('-', ',')).map(_.toInt)
        (start1 to end1, start2 to end2)
      }
      .count { case (range1, range2) =>
        range1.containsSlice(range2) || range2.containsSlice(range1)
      }
  }

  def part2(input: String): Int = {
    input.linesIterator
      .map { line =>
        val Array(start1, end1, start2, end2) = line.split(Array('-', ',')).map(_.toInt)
        (start1 to end1, start2 to end2)
      }
      .count { case (range1, range2) =>
        (range1 intersect range2).nonEmpty
      }
  }
}
