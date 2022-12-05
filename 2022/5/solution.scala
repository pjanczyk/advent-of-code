import java.nio.file.{Files, Path}
import scala.collection.mutable
import scala.util.matching.Regex

object day5 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt"))
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): String = {
    val stacks = parseStacks(input)
    val moves = parseMoves(input)

    moves.foreach { case (count, from, to) =>
      (0 until count).foreach { _ =>
        stacks(to).push(stacks(from).pop())
      }
    }

    stacks.map(_.top).mkString
  }

  def part2(input: String): String = {
    val stacks = parseStacks(input)
    val moves = parseMoves(input)

    moves.foreach { case (count, from, to) =>
      val items = Seq.fill(count)(stacks(from).pop)
      stacks(to).pushAll(items.reverse)
    }

    stacks.map(_.top).mkString
  }

  private def parseStacks(input: String): IndexedSeq[mutable.Stack[String]] = {
    val rows = input.linesIterator
      .takeWhile(_.contains("["))
      .map(_.drop(1).sliding(size = 1, step = 4).toSeq)
      .toSeq

    rows.transpose
      .map(stack => stack.filter(!_.isBlank).to(mutable.Stack))
      .toIndexedSeq
  }

  private def parseMoves(input: String) = {
    val regex = """move (\d+) from (\d+) to (\d+)""".r
    input.linesIterator
      .withFilter(regex.matches(_))
      .map { case regex(count, from, to) =>
        (count.toInt, from.toInt - 1, to.toInt - 1)
      }
  }
}
