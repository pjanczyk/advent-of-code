package day3

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  parseInput(input)
    .collect { case Instruction.Mul(a, b) => a * b }
    .sum
}

def part2(input: String): Int = {
  var enabled = true
  var sum = 0
  parseInput(input).collect {
    case Instruction.Do => enabled = true
    case Instruction.Dont => enabled = false
    case Instruction.Mul(a, b) if enabled => sum += a * b
  }
  sum
}

enum Instruction {
  case Mul(a: Int, b: Int)
  case Do
  case Dont
}

def parseInput(input: String): List[Instruction] = {
  val regex = """mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)""".r
  regex.findAllIn(input)
    .map {
      case "do()" => Instruction.Do
      case "don't()" => Instruction.Dont
      case s"mul($a,$b)" => Instruction.Mul(a.toInt, b.toInt)
    }
    .toList
}
