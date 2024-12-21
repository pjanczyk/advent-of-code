package day19

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable
import scala.util.matching.Regex

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  val (towels, patterns) = parseInput(input)
  val regex = towels.map(Regex.quote).mkString("(", "|", ")*").r
  patterns.count { pattern => regex.matches(pattern) }
}

def part2(input: String): Long = {
  val (towels, patterns) = parseInput(input)
  patterns
    .map { pattern => countArrangements(towels, pattern) }
    .sum
}

def parseInput(input: String): (List[String], List[String]) = {
  val Array(towelsStr, patternsStr) = input.split("\n\n")
  val towels = towelsStr.split(", ").toList
  val patterns = patternsStr.linesIterator.toList
  (towels, patterns)
}

def countArrangements(towels: List[String], pattern: String, cache: mutable.Map[String, Long] = mutable.Map()): Long = {
  if (pattern.isEmpty) {
    1
  } else {
    cache.getOrElseUpdate(pattern, {
      towels
        .filter { towel => pattern.startsWith(towel) }
        .map { towel => countArrangements(towels, pattern.stripPrefix(towel), cache) }
        .sum
    })
  }
}
