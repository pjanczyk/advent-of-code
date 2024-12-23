package day23

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  val connectionList = parseInput(input)
  val connectionMap = (connectionList ++ connectionList.map(_.swap)).groupMap(_._1)(_._2)

  val interconnectedSets =
    connectionList
      .flatMap { (a, b) =>
        (connectionMap(a) intersect connectionMap(b))
          .map { c => Set(a, b, c) }
      }
      .distinct

  interconnectedSets.count { set => set.exists(_.startsWith("t")) }
}

def part2(input: String): String = {
  val connectionList = parseInput(input)
  val connectionMap = (connectionList ++ connectionList.map(_.swap)).groupMap(_._1)(_._2)

  def findLargestInterconnectedSet(interconnectedSet: Set[String]): Set[String] = {
    interconnectedSet
      .map(connectionMap)
      .reduce(_ intersect _)
      .filter(_ > interconnectedSet.max)
      .map { common => findLargestInterconnectedSet(interconnectedSet + common) }
      .maxByOption(_.size)
      .getOrElse(interconnectedSet)
  }

  val largestInterconnectedSet = connectionList
    .map { (a, b) => findLargestInterconnectedSet(Set(a, b)) }
    .maxBy(_.size)

  largestInterconnectedSet.toList.sorted.mkString(",")
}

def parseInput(input: String): List[(String, String)] = {
  input.linesIterator.map { case s"$a-$b" => (a, b) }.toList
}
