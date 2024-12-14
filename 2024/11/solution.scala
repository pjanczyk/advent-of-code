package day11

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(solution(input, blinks = 25))
  println(solution(input, blinks = 75))
}

def solution(input: String, blinks: Int): Long = {
  val stones = parseInput(input)
  stones.map { stone => countStones(stone, blinks) }.sum
}

def parseInput(input: String): List[Long] = {
  input.split(' ').map(_.toLong).toList
}

val cache = mutable.Map[(Long, Int), Long]()

def countStones(stone: Long, blinks: Int): Long = {
  if (blinks == 0) {
    1
  } else {
    cache.getOrElseUpdate((stone, blinks), {
      stone match {
        case 0L => countStones(1, blinks - 1)
        case EvenNumberOfDigits(l, r) => countStones(l, blinks - 1) + countStones(r, blinks - 1)
        case stone => countStones(stone * 2024, blinks - 1)
      }
    })
  }
}

object EvenNumberOfDigits {
  def unapply(stone: Long): Option[(Long, Long)] = {
    val digits = stone.toString
    Option.when(digits.length % 2 == 0) {
      val (l, r) = digits.splitAt(digits.length / 2)
      (l.toLong, r.toLong)
    }
  }
}
