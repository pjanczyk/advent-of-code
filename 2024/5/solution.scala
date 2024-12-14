package day5

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val (rules, updates) = parseInput(input)
  updates
    .filter { update => isOrdered(update, rules) }
    .map { update => update(update.size / 2) }
    .sum
}

def part2(input: String): Int = {
  val (rules, updates) = parseInput(input)
  updates
    .filterNot { update => isOrdered(update, rules) }
    .map { update => reorder(update, rules) }
    .map { update => update(update.size / 2) }
    .sum
}

def isOrdered(update: Seq[Int], rules: List[(Int, Int)]) = {
  rules.forall { (pageL, pageR) =>
    (update.indexOf(pageL), update.indexOf(pageR)) match {
      case (-1, _) | (_, -1) => true
      case (indexL, indexR) => indexL < indexR
    }
  }
}

def reorder(update: List[Int], rules: List[(Int, Int)]): List[Int] = {
  val result = update.toArray
  while (!isOrdered(result, rules)) {
    rules.foreach { (pageL, pageR) =>
      (result.indexOf(pageL), result.indexOf(pageR)) match {
        case (-1, _) | (_, -1) =>
        case (indexL, indexR) =>
          if (indexL > indexR) {
            result(indexL) = pageR
            result(indexR) = pageL
          }
      }
    }
  }
  result.toList
}

def parseInput(input: String): (List[(Int, Int)], List[List[Int]]) = {
  val Array(rulesStr, updatesStr) = input.split("\n\n")
  val rules = rulesStr.linesIterator.map { case s"$l|$r" => (l.toInt, r.toInt) }.toList
  val updates = updatesStr.linesIterator.map { rule => rule.split(',').toList.map(_.toInt) }.toList
  (rules, updates)
}
