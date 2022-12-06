import java.nio.file.{Files, Path}
import scala.collection.mutable
import scala.util.matching.Regex

object day6 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int =
    input.sliding(4).indexWhere(chunk => chunk == chunk.distinct) + 4

  def part2(input: String): Int =
    input.sliding(14).indexWhere(chunk => chunk == chunk.distinct) + 14
}
