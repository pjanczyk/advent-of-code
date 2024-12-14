package day2

import day1.part2
import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  parseInput(input)
    .count(report => isSafe(report))
}

def part2(input: String): Int = {
  parseInput(input)
    .count(report => isSafe(report) || withOneLevelRemoved(report).exists(isSafe))
}

def parseInput(input: String): List[List[Int]] = {
  input.linesIterator
    .map(_.split(' ').map(_.toInt).toList)
    .toList
}

def isSafe(report: List[Int]): Boolean = {
  val deltas = report.sliding(2).collect { case List(a, b) => a - b }.toList
  deltas.forall((1 to 3).contains) ||
    deltas.forall((-3 to -1).contains)
}

def withOneLevelRemoved(report: List[Int]): Iterable[List[Int]] =
  report.indices.map(index => report.patch(index, Nil, 1))
