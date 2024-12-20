package day17

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): String = {
  val program = parseInput(input)
  interpret(program).mkString(",")
}

def part2(input: String): Long = {
  // 2,4,   bst B = A % 8
  // 1,1,   bxl B = B ^ 1
  // 7,5,   cdv C = A / 2^B
  // 1,5,   bxl B = B ^ 5
  // 4,0,   bxc B = B ^ C
  // 0,3,   adv A = A / 2^3
  // 5,5,   out print(B % 8)
  // 3,0    jnz while(A != 0)

  // var A = ???;
  // do {
  //   print((A ^ 0b100 ^ (A >> (A & 0b111 ^ 0b001))) & 0b111);
  //   A = A >> 3;
  // } while (A != 0);

  val program = parseInput(input)
  val expectedOutput = program.program

  var a = 0L
  (1 to expectedOutput.size).foreach { digit =>
    a = a << 3
    while (
      interpret(program.copy(a = a)) != expectedOutput.takeRight(digit)
    ) {
      a += 1
    }
  }
  a
}

case class Program(a: Long, b: Long, c: Long, program: List[Int])

def parseInput(input: String): Program = {
  input.linesIterator.toList match {
    case List(s"Register A: $a", s"Register B: $b", s"Register C: $c", "", s"Program: $program") =>
      Program(a.toLong, b.toLong, c.toLong, program.split(',').map(_.toInt).toList)
  }
}

def interpret(program: Program): List[Long] = {
  var ip = 0
  var a = program.a
  var b = program.b
  var c = program.c
  val out = mutable.ListBuffer[Long]()

  while (ip + 1 < program.program.length) {
    val instr = program.program(ip)
    val literal = program.program(ip + 1)
    val combo = literal match {
      case 0 | 1 | 2 | 3 => literal.toLong
      case 4 => a
      case 5 => b
      case 6 => c
    }
    ip += 2
    instr match {
      case 0 => a = a / (1 << combo)
      case 1 => b = b ^ literal
      case 2 => b = combo % 8
      case 3 => if (a != 0) ip = literal
      case 4 => b = b ^ c
      case 5 => out += combo % 8
      case 6 => b = a / (1 << combo)
      case 7 => c = a / (1 << combo)
    }
  }

  out.toList
}
