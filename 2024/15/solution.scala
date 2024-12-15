package day15

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val (area, moves) = parseInput(input)
  val finalArea = simulateMoves(area, moves)
  println(finalArea)
  finalArea.positionOf('O').map(pos => pos.x + 100 * pos.y).sum
}

def part2(input: String): Int = {
  val (area, moves) = parseInput(input)
  val scaledUpArea = scaleUp(area)
  val finalArea = simulateMoves(scaledUpArea, moves)
  println(finalArea)
  finalArea.positionOf('[').map(pos => pos.x + 100 * pos.y).sum
}

def simulateMoves(initialArea: Area, moves: List[Move]): Area = {
  moves.foldLeft(initialArea) { (area, move) =>
    val robotPos = area.positionOf('@').head
    simulateMove(area, robotPos, move.dir)
      .getOrElse(area)
  }
}

def simulateMove(area: Area, pos: Vec, dir: Vec): Option[Area] = {
  area.at(pos) match {
    case '#' => None
    case '.' => Some(area)
    case '[' if dir.y != 0 =>
      simulateMove(area, pos + dir, dir)
        .flatMap(updatedArea => simulateMove(updatedArea, pos + dir + Vec(1, 0), dir))
        .map(updatedArea =>
          updatedArea
            .updated(pos + dir, '[')
            .updated(pos + dir + Vec(1, 0), ']')
            .updated(pos, '.')
            .updated(pos + Vec(1, 0), '.')
        )
    case ']' if dir.y != 0 =>
      simulateMove(area, pos - Vec(1, 0), dir)
    case '[' | ']' | '@' | 'O' =>
      simulateMove(area, pos + dir, dir)
        .map(updatedArea =>
          updatedArea
            .updated(pos + dir, updatedArea.at(pos))
            .updated(pos, '.')
        )
  }
}

def parseInput(input: String): (Area, List[Move]) = {
  val Array(areaStr, movesStr) = input.split("\n\n")
  val area = Area(areaStr.linesIterator.toVector.map(_.toVector))
  val moves = movesStr.linesIterator.mkString.toList.map(Move(_))
  (area, moves)
}

def scaleUp(area: Area): Area = {
  Area(
    area.matrix.map { line =>
      line.flatMap {
        case '#' => "##"
        case 'O' => "[]"
        case '.' => ".."
        case '@' => "@."
      }
    }
  )
}

case class Move(c: Char) {
  val dir: Vec = c match {
    case '>' => Vec(1, 0)
    case '<' => Vec(-1, 0)
    case '^' => Vec(0, -1)
    case 'v' => Vec(0, 1)
  }
}

case class Area(matrix: Vector[Vector[Char]]) {
  def at(pos: Vec): Char =
    matrix(pos.y)(pos.x)

  def positionOf(c: Char): Seq[Vec] =
    for {
      y <- matrix.indices
      x <- matrix.head.indices
      if matrix(y)(x) == c
    } yield Vec(x, y)

  def updated(pos: Vec, c: Char): Area =
    Area(
      matrix.updated(
        pos.y,
        matrix(pos.y).updated(pos.x, c)
      )
    )

  override def toString: String =
    matrix
      .map(_.mkString)
      .mkString("\n")
}

case class Vec(x: Int, y: Int) {
  def +(o: Vec): Vec = Vec(x + o.x, y + o.y)
  def -(o: Vec): Vec = Vec(x - o.x, y - o.y)
}
