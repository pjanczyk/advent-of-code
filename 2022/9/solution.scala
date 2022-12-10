import java.nio.file.{Files, Path}
import scala.collection.mutable

object day9 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val instruction = """([LRUD]) (\d+)\s*""".r

    var headX = 0
    var headY = 0
    var tailX = 0
    var tailY = 0
    val visited = mutable.Set[(Int, Int)]()

    visited.add((0, 0))

    input.linesIterator
      .foreach { case instruction(direction, count) =>
        (0 until count.toInt).foreach { _ =>
          direction match {
            case "L" => headX -= 1
            case "R" => headX += 1
            case "U" => headY -= 1
            case "D" => headY += 1
          }

          val diffX = headX - tailX
          val diffY = headY - tailY

          if (Math.abs(diffX) >= 2 || Math.abs(diffY) >= 2) {
            tailX += Math.signum(diffX).toInt
            tailY += Math.signum(diffY).toInt

            visited.add((tailX, tailY))
          }
        }
      }
    visited.size
  }

  def part2(input: String): Int = {
    val instruction = """([LRUD]) (\d+)\s*""".r

    val positions = mutable.Seq.fill(10)((0, 0))
    val visited = mutable.Set[(Int, Int)]()
    visited.add((0, 0))

    input.linesIterator
      .foreach { case instruction(direction, count) =>
        (0 until count.toInt).foreach { _ =>
          direction match {
            case "L" => positions(0) = (positions(0)._1 - 1, positions(0)._2)
            case "R" => positions(0) = (positions(0)._1 + 1, positions(0)._2)
            case "U" => positions(0) = (positions(0)._1, positions(0)._2 - 1)
            case "D" => positions(0) = (positions(0)._1, positions(0)._2 + 1)
          }

          positions.indices.drop(1).foreach { i =>
            val diffX = positions(i - 1)._1 - positions(i)._1
            val diffY = positions(i - 1)._2 - positions(i)._2

            if (Math.abs(diffX) >= 2 || Math.abs(diffY) >= 2) {
              positions(i) = (
                positions(i)._1 + Math.signum(diffX).toInt,
                positions(i)._2 + Math.signum(diffY).toInt
              )
            }
          }
          visited.add(positions.last)
        }
      }
    visited.size
  }
}
