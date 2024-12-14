package day8

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val area = parseInput(input)
  area.antennas
    .groupBy(_.frequency)
    .iterator
    .flatMap { (frequency, antennas) => antennas.combinations(2) }
    .flatMap { case List(antenna1, antenna2) =>
      val direction = antenna2.position - antenna1.position
      List(
        antenna1.position - direction,
        antenna2.position + direction
      ).filter(area.contains)
    }
    .toSet
    .size
}

def part2(input: String): Int = {
  val area = parseInput(input)
  area.antennas
    .groupBy(_.frequency)
    .iterator
    .flatMap { (frequency, antennas) => antennas.combinations(2) }
    .flatMap { case List(antenna1, antenna2) =>
      val direction = antenna2.position - antenna1.position
      Iterator.iterate(antenna1.position)(_ - direction).takeWhile(area.contains) ++
        Iterator.iterate(antenna2.position)(_ + direction).takeWhile(area.contains)
    }
    .toSet
    .size
}

def parseInput(input: String): Area = {
  Area(
    width = input.linesIterator.next.length,
    height = input.linesIterator.size,
    antennas = for {
      (line, y) <- input.linesIterator.toList.zipWithIndex
      (frequency, x) <- line.zipWithIndex
      if frequency != '.'
    } yield Antenna(frequency, Position(x, y))
  )
}

case class Area(width: Int, height: Int, antennas: List[Antenna]) {
  def contains(pos: Position): Boolean =
    (0 until width).contains(pos.x) && (0 until height).contains(pos.y)
}

case class Antenna(frequency: Frequency, position: Position)

type Frequency = Char

case class Position(x: Int, y: Int) {
  def +(dir: Direction): Position = Position(x + dir.x, y + dir.y)
  def -(dir: Direction): Position = Position(x - dir.x, y - dir.y)
  def -(pos: Position): Direction = Direction(x - pos.x, y - pos.y)
}

case class Direction(x: Int, y: Int)
