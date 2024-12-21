package day18

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

val size = 70
val topLeft = Position(0, 0)
val bottomRight = Position(size, size)

def part1(input: String): Int = {
  val obstacles = parseInput(input)
  val area = Area(size, obstacles.take(1024).toSet)
  shortestDistance(area, topLeft, bottomRight)
}

def part2(input: String): Position = {
  val obstacles = parseInput(input)
  obstacles.indices
    .map { time => obstacles.take(time) }
    .find { obstaclesAtTime => !isConnected(Area(size, obstaclesAtTime.toSet), topLeft, bottomRight) }
    .get
    .last
}

def parseInput(input: String): List[Position] = {
  input.linesIterator.map { case s"$x,$y" => Position(x.toInt, y.toInt) }.toList
}

case class Position(x: Int, y: Int) {
  def adjacent: List[Position] = List(
    Position(x + 1, y),
    Position(x - 1, y),
    Position(x, y + 1),
    Position(x, y - 1),
  )
}

case class Area(size: Int, obstacles: Set[Position]) {
  lazy val freePositions: Set[Position] =
    (for {
      y <- 0 to size
      x <- 0 to size
      pos = Position(x, y)
      if !obstacles.contains(pos)
    } yield pos).toSet
}

def shortestDistance(area: Area, source: Position, target: Position): Int = {
  val prev = mutable.Map[Position, Position]()
  val dist = mutable.Map[Position, Int]()
    .addOne(source -> 0)
    .withDefaultValue(Int.MaxValue / 2)
  val queue = mutable.Set[Position]()
    .addAll(area.freePositions)

  while (queue.nonEmpty) {
    val pos = queue.minBy(dist)
    queue.remove(pos)

    pos.adjacent
      .filter(queue.contains)
      .foreach { adjacent =>
        val alt = dist(pos) + 1
        if (alt < dist(adjacent)) {
          dist(adjacent) = alt
          prev(adjacent) = pos
        }
      }
  }

  dist(target)
}

def isConnected(area: Area, source: Position, target: Position): Boolean = {
  val stack = mutable.Stack(source)
  val visited = mutable.Set[Position](source)

  while (stack.nonEmpty) {
    val pos = stack.pop()
    if (pos == target) return true
    val adjacent = pos.adjacent
      .filter(!visited.contains(_))
      .filter(area.freePositions.contains)
    stack.addAll(adjacent)
    visited.addAll(adjacent)
  }
  false
}