package day6

import java.nio.file.Files
import java.nio.file.Path
import scala.annotation.targetName
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val area = parseInput(input)
  simulateMovement(area).distinct.size
}

def part2(input: String): Int = {
  val area = parseInput(input)
  simulateMovement(area)
    .distinct
    .drop(1)
    .count { pos =>
      val withObstacle = area.updated(pos, '#')
      simulateMovement(withObstacle).isEmpty
    }
}

def simulateMovement(area: Area): Vector[Position] = {
  var dir = Direction(0, -1)
  var pos = area.positionOf('^').head
  var path = Vector.empty[Position]
  var visited = Set.empty[(Position, Direction)]
  while (true) {
    path = path.appended(pos)
    visited += (pos, dir)
    area.at(pos + dir) match {
      case Some('#') =>
        dir = dir.rotateRight
      case Some('.' | '^') =>
        pos = pos + dir
        if (visited.contains((pos, dir))) return Vector.empty // cycle
      case None =>
        return path
    }
  }
  assert(false)
}

def parseInput(input: String): Area = {
  val matrix = input.linesIterator.toVector.map(_.toVector)
  Area(matrix)
}

case class Area(matrix: Vector[Vector[Char]]) {
  def at(pos: Position): Option[Char] =
    matrix.lift(pos.y).flatMap(_.lift(pos.x))

  def positionOf(c: Char): Seq[Position] =
    for {
      y <- matrix.indices
      x <- matrix(y).indices
      if matrix(y)(x) == c
    } yield Position(x, y)

  def updated(pos: Position, c: Char): Area =
    Area(
      matrix.updated(
        pos.y,
        matrix(pos.y).updated(pos.x, c)
      )
    )
}

case class Position(x: Int, y: Int) {
  def +(dir: Direction): Position = Position(x + dir.x, y + dir.y)
}

case class Direction(x: Int, y: Int) {
  def rotateRight: Direction = Direction(-y, x)
}
