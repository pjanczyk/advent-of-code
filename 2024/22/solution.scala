package day22

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  var secrets = parseInput(input)
  (1 to 2000).foreach { _ =>
    secrets = secrets.map(nextSecret)
  }
  secrets.sum
}

def part2(input: String): Long = {
  val secrets = parseInput(input)

  val changeSequenceToPrice = secrets
    .flatMap { secret =>
      (1 to 2000)
        .scanLeft(secret)((s, _) => nextSecret(s))
        .map(_ % 10)
        .sliding(5)
        .map { priceWindow =>
          val changeSequence = priceWindow.sliding(2).map { case Seq(a, b) => b - a }.toList
          val price = priceWindow.last
          changeSequence -> price
        }
        .distinctBy { (changeSequence, _) => changeSequence }
    }
    .groupMapReduce(_._1)(_._2)(_ + _)

  changeSequenceToPrice.values.max
}

def parseInput(input: String): List[Long] = {
  input.linesIterator.map(_.toLong).toList
}

def nextSecret(secret: Long): Long = {
  var s = secret
  s = (s ^ (s * 64)) % 16777216
  s = (s ^ (s / 32)) % 16777216
  s = (s ^ (s * 2048)) % 16777216
  s
}
