package day7

import java.nio.file.Files
import java.nio.file.Path

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  val equations = parseInput(input)
  println(solution(equations, operators = _ * _, _ + _))
  println(solution(equations, operators = _ * _, _ + _, _ || _))
}

def solution(equations: List[Equation], operators: (Long, Long) => Long*): Long = {
  def eval(operands: Vector[Long]): Iterable[Long] = {
    if (operands.size == 1) {
      operands
    } else {
      for {
        lhs <- eval(operands.dropRight(1))
        rhs = operands.last
        operator <- operators
      } yield operator(lhs, rhs)
    }
  }

  equations
    .filter { equation =>
      eval(equation.operands).exists(_ == equation.result)
    }
    .map(_.result)
    .sum
}

extension (x: Long) {
  def ||(y: Long): Long = s"$x$y".toLong
}

case class Equation(result: Long, operands: Vector[Long])

def parseInput(input: String): List[Equation] = {
  input.linesIterator.toList
    .map { case s"$result: $operands" =>
      Equation(
        result.toLong,
        operands.split(' ').map(_.toLong).toVector
      )
    }
}
