package day10

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val area = parseInput(input)
  area.trailheads
    .map { trailhead => findTrailEnds(area, trailhead).toSet.size }
    .sum
}

def part2(input: String): Int = {
  val area = parseInput(input)
  area.trailheads
    .map { trailhead => findTrailEnds(area, trailhead).size }
    .sum
}

def parseInput(input: String): Area = {
  val matrix = input.linesIterator.toVector.map(_.toVector.map(_.toString.toInt))
  Area(matrix)
}

def findTrailEnds(area: Area, currentPos: Position): Iterator[Position] = {
  val currentHeight = area.at(currentPos).get
  if (currentHeight == 9) {
    Iterator(currentPos)
  } else {
    Direction.upDownLeftRight
      .iterator
      .map(direction => currentPos + direction)
      .filter(nextPos => area.at(nextPos).contains(currentHeight + 1))
      .flatMap(nextPos => findTrailEnds(area, nextPos))
  }
}

case class Area(matrix: Vector[Vector[Int]]) {
  def at(pos: Position): Option[Int] =
    matrix.lift(pos.y).flatMap(_.lift(pos.x))

  def trailheads: Seq[Position] =
    for {
      y <- matrix.indices
      x <- matrix(y).indices
      if matrix(y)(x) == 0
    } yield Position(x, y)
}

case class Position(x: Int, y: Int) {
  def +(dir: Direction): Position = Position(x + dir.x, y + dir.y)
}

case class Direction(x: Int, y: Int)

object Direction {
  val upDownLeftRight: List[Direction] =
    List(Direction(0, 1), Direction(0, -1), Direction(-1, 0), Direction(1, 0))
}
