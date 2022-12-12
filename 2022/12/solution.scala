import java.nio.file.{Files, Path}
import scala.collection.mutable

object day12 {
  def main(args: Array[String]): Unit = {
    val input = Files.readString(Path.of("input.txt")).trim
    println(part1(input))
    println(part2(input))
  }

  def part1(input: String): Int = {
    val heightmap = parseHeightmap(input)
    findShortestPath(
      heightmap,
      start = 'S',
      end = 'E',
      canMove = { (from, to) => heightOf(from) >= heightOf(to) - 1 }
    )
  }

  def part2(input: String): Int = {
    val heightmap = parseHeightmap(input)
    findShortestPath(
      heightmap,
      start = 'E',
      end = 'a',
      canMove = { (from, to) => heightOf(to) >= heightOf(from) - 1 }
    )
  }

  private type Pos = (Int, Int)
  private type Heightmap = Map[Pos, Char]

  private def parseHeightmap(input: String): Heightmap =
    input.linesIterator.zipWithIndex.flatMap { case (line, y) =>
      line.zipWithIndex.map { case (height, x) => (x, y) -> height }
    }.toMap

  private def findShortestPath(
      heightmap: Heightmap,
      start: Char,
      end: Char,
      canMove: (Char, Char) => Boolean
  ): Int = {
    val queue = mutable.Queue[Pos]()
    val visited = mutable.Set[Pos]()
    val stepsTo = mutable.Map[Pos, Int]()

    val startPos: Pos = heightmap.find(_._2 == start).get._1
    visited.add(startPos)
    stepsTo.put(startPos, 0)
    queue.enqueue(startPos)

    while (heightmap(queue.head) != end) {
      val pos = queue.dequeue()

      val neighbours = Seq((1, 0), (-1, 0), (0, 1), (0, -1))
        .map(delta => (pos._1 + delta._1, pos._2 + delta._2))
        .filter(heightmap.isDefinedAt)
        .filter(!visited.contains(_))
        .filter(neighbour => canMove(heightmap(pos), heightmap(neighbour)))

      neighbours.foreach { neighbour =>
        visited.add(neighbour)
        stepsTo.put(neighbour, stepsTo(pos) + 1)
        queue.enqueue(neighbour)
      }
    }
    stepsTo(queue.head)
  }

  private def heightOf(symbol: Char): Int =
    symbol match {
      case 'S'   => 'a'.toInt
      case 'E'   => 'z'.toInt
      case other => other.toInt
    }
}
