package day1

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val (ls, rs) = parseInput(input).unzip
  (ls.sorted zip rs.sorted)
    .map { case (l, r) => Math.abs(l - r) }
    .sum
}

def part2(input: String): Int = {
  val (ls, rs) = parseInput(input).unzip
  ls.map(l => l * rs.count(_ == l))
    .sum
}

def parseInput(input: String): List[(Int, Int)] = {
  input.linesIterator
    .map { case s"$l $r" => (l.trim.toInt, r.trim.toInt) }
    .toList
}
