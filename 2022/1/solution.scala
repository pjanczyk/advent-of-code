import java.nio.file.{Files, Path}
import scala.jdk.StreamConverters._

object day1 {
  def part1(input: String): Int = {
    input.split("\n\n").toSeq
      .map(chunk => chunk.linesIterator.map(_.toInt).sum)
      .max
  }

  def part2(input: String): Int = {
    input.split("\n\n").toSeq
      .map(chunk => chunk.linesIterator.map(_.toInt).sum)
      .sorted
      .takeRight(3)
      .sum
  }

  def main(args: Array[String]): Unit = {
    val input = Files.lines(Path.of("input.txt")).toScala(Iterator).mkString("\n")
    println(part1(input))
    println(part2(input))
  }
}
