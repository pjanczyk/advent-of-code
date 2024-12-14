package day12

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val garden = parseInput(input)
  detectRegions(garden)
    .map { region => region.area * region.perimeter }
    .sum
}

def part2(input: String): Int = {
  val garden = parseInput(input)
  detectRegions(garden)
    .map { region => region.area * region.sides }
    .sum
}

def parseInput(input: String): Garden = {
  val matrix = input.linesIterator.toVector.map(_.toVector)
  Garden(matrix)
}

def detectRegions(garden: Garden): List[Region] = {
  val visited = mutable.Set[Position]()

  def dfs(pos: Position): Iterator[Position] = {
    visited.add(pos)
    val adjacent = Direction.upDownLeftRight.iterator
      .map { dir => pos + dir }
      .filter { adjacent => !visited(adjacent) && garden.at(adjacent) == garden.at(pos) }
    Iterator(pos) ++ adjacent.flatMap(dfs)
  }

  garden.positions
    .collect {
      case position if !visited(position) => Region(dfs(position).toSet)
    }
    .toList
}

case class Garden(matrix: Vector[Vector[Char]]) {
  def at(pos: Position): Option[Char] =
    matrix.lift(pos.y).flatMap(_.lift(pos.x))

  def positions: Iterable[Position] =
    for {
      y <- matrix.indices
      x <- matrix(y).indices
    } yield Position(x, y)
}

case class Region(positions: Set[Position]) {
  def area: Int =
    positions.size

  def perimeter: Int =
    positions.iterator
      .map { pos =>
        Direction.upDownLeftRight.count { dir => !positions.contains(pos + dir) }
      }
      .sum

  def sides: Int =
    positions.iterator
      .map { pos =>
        Direction.upDownLeftRight.count { dir =>
          !positions.contains(pos + dir) &&
            !(positions.contains(pos + dir.rotateRight) && !positions.contains(pos + dir + dir.rotateRight))
        }
      }
      .sum
}

case class Position(x: Int, y: Int) {
  def +(dir: Direction): Position = Position(x + dir.x, y + dir.y)
}

case class Direction(x: Int, y: Int) {
  def rotateRight: Direction = Direction(-y, x)
}

object Direction {
  val upDownLeftRight: List[Direction] =
    List(Direction(0, 1), Direction(0, -1), Direction(-1, 0), Direction(1, 0))
}
