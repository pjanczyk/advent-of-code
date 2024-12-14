package day4

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val xmas = "XMAS".toList
  val puzzle = parseInput(input)
  (for {
    x <- puzzle.xs
    y <- puzzle.ys
    dx <- List(-1, 0, 1)
    dy <- List(-1, 0, 1)
    fragment = xmas.indices.map(i => puzzle(x + i * dx, y + i * dy))
    if fragment == xmas
  } yield ()).size
}

def part2(input: String): Int = {
  val puzzle = parseInput(input)
  (for {
    x <- puzzle.xs
    y <- puzzle.ys
    if puzzle(x + 1, y + 1) == 'A' &&
      Set(puzzle(x, y), puzzle(x + 2, y + 2)) == Set('M', 'S') &&
      Set(puzzle(x, y + 2), puzzle(x + 2, y)) == Set('M', 'S')
  } yield ()).size
}

trait Puzzle {
  def xs: Range
  def ys: Range
  def apply(x: Int, y: Int): Char
}

def parseInput(input: String): Puzzle = {
  val matrix = input.linesIterator.toVector.map(_.toVector)
  new Puzzle {
    override def xs: Range = matrix.head.indices
    override def ys: Range = matrix.indices
    override def apply(x: Int, y: Int): Char =
      if (xs.contains(x) && ys.contains(y)) matrix(y)(x)
      else '.'
  }
}
