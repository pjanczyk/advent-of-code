package day13

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Int = {
  val machines = parseInput(input)
  machines
    .flatMap { machine =>
      (for {
        pushesA <- (0 to 100)
        pushesB <- (0 to 100)
        if (machine.buttonA * pushesA) + (machine.buttonB * pushesB) == machine.prize
      } yield pushesA * 3 + pushesB).minOption
    }
    .sum
}

def part2(input: String): Long = {
  val machines = parseInput(input)
  machines
    .flatMap { machine =>
      val A = machine.buttonA
      val B = machine.buttonB
      val C = machine.prize + Vec(10000000000000L, 10000000000000L)

      // A*x + B*y = C
      //
      // A1*x + B1*y = C1
      // A2*x + B2*y = C2
      //
      // x = (C2*B1 - C1*B2) / (A2*B2 - C1*B2)
      // y = (C1 - A1*x) / B1

      val x = (C.y * B.x - C.x * B.y) / (A.y * B.x - A.x * B.y)
      val y = (C.x - A.x * x) / B.x

      Option.when(A * x + B * y == C) {
        x * 3 + y
      }
    }
    .sum
}

case class Machine(buttonA: Vec, buttonB: Vec, prize: Vec)

case class Vec(x: Long, y: Long) {
  def +(d: Vec): Vec = Vec(x + d.x, y + d.y)
  def *(f: Long): Vec = Vec(x * f, y * f)
}

def parseInput(input: String): List[Machine] = {
  input.split("\n\n").toList
    .map(_.linesIterator.toList)
    .map {
      case List(s"Button A: X+$ax, Y+$ay", s"Button B: X+$bx, Y+$by", s"Prize: X=$px, Y=$py") =>
        Machine(
          buttonA = Vec(ax.toLong, ay.toLong),
          buttonB = Vec(bx.toLong, by.toLong),
          prize = Vec(px.toLong, py.toLong)
        )
    }
}
