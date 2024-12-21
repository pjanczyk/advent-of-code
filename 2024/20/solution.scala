package day20

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable
import scala.math.abs

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  val area = parseInput(input)
  println(countCheats(area, maxLength = 2, minSave = 100))
  println(countCheats(area, maxLength = 20, minSave = 100))
}

def countCheats(area: Area, maxLength: Int, minSave: Int): Int = {
  val start = area.positionOf('S').head
  val end = area.positionOf('E').head

  val prev = mutable.Map[Position, Position]()
  val dist = mutable.Map[Position, Int]()
    .addOne(start -> 0)
    .withDefaultValue(Int.MaxValue)
  val queue = mutable.Set[Position]()
    .addAll(area.positionOf('S') ++ area.positionOf('.') ++ area.positionOf('E'))

  while (queue.nonEmpty) {
    val pos = queue.minBy(dist(_))
    queue.remove(pos)

    pos.adjacent(distance = 1)
      .filter { adjacent => queue.contains(adjacent) }
      .foreach { adjacent =>
        val alt = dist(pos) + 1
        if (alt < dist(adjacent)) {
          dist(adjacent) = alt
          prev(adjacent) = pos
        }
      }
  }

  val cheats =
    for {
      cheatStart <- dist.keys
      cheatLength <- 1 to maxLength
      cheatEnd <- cheatStart.adjacent(cheatLength)
      if dist.contains(cheatEnd)
      save = dist(cheatEnd) - dist(cheatStart) - cheatLength
      if save >= minSave
    } yield (cheatStart, cheatEnd)

  cheats.size
}

def parseInput(input: String): Area = {
  Area(input.linesIterator.toVector.map(_.toVector))
}

case class Area(matrix: Vector[Vector[Char]]) {
  def positionOf(c: Char): Seq[Position] =
    for {
      y <- matrix.indices
      x <- matrix.head.indices
      if matrix(y)(x) == c
    } yield Position(x, y)
}

case class Position(x: Int, y: Int) {
  def adjacent(distance: Int): Seq[Position] =
    for {
      dx <- -distance to distance
      dy <- Set(-(distance - abs(dx)), distance - abs(dx))
    } yield Position(x + dx, y + dy)
}
