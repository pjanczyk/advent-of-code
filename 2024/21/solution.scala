package day21

import java.nio.file.Files
import java.nio.file.Path
import scala.collection.mutable

@main def main(): Unit = {
  val input = Files.readString(Path.of("input.txt")).trim
  println(part1(input))
  println(part2(input))
}

def part1(input: String): Long = {
  val keypads = List(numericKeypad, directionalKeypad, directionalKeypad)
  val codes = parseInput(input)
  codes.map(codeComplexity(_, keypads)).sum
}

def part2(input: String): Long = {
  val keypads = List(numericKeypad) ++ List.fill(25)(directionalKeypad)
  val codes = parseInput(input)
  codes.map(codeComplexity(_, keypads)).sum
}

def codeComplexity(code: String, keypads: List[Keypad]): Long = {
  val buttonPresses = countButtonPresses(code, keypads)
  val numericValue = code.filter(_.isDigit).toInt
  buttonPresses * numericValue
}

val countButtonPressesCache: mutable.Map[(String, List[Keypad]), Long] = mutable.Map()

def countButtonPresses(code: String, keypads: List[Keypad]): Long = {
  countButtonPressesCache.getOrElseUpdate((code, keypads), {
    keypads match {
      case keypad :: nextKeypads =>
        code.split("(?<=A)")
          .map { codePart =>
            typeOnKeypad(keypad, keypad.positionOf('A'), codePart)
              .map(possibleSequence => countButtonPresses(possibleSequence, nextKeypads))
              .min
          }
          .sum
      case Nil => code.length
    }
  })
}

def typeOnKeypad(keypad: Keypad, position: Position, code: String): List[String] = {
  if (code == "") {
    List("")
  } else if (position == keypad.positionOf(' ')) {
    Nil
  } else if (position == keypad.positionOf(code.head)) {
    typeOnKeypad(keypad, position, code.tail).map("A" + _)
  } else {
    val target = keypad.positionOf(code.head)
    List(
      Option.when(target.x > position.x) {
        typeOnKeypad(keypad, position.right, code).map(">" + _)
      },
      Option.when(target.x < position.x) {
        typeOnKeypad(keypad, position.left, code).map("<" + _)
      },
      Option.when(target.y > position.y) {
        typeOnKeypad(keypad, position.down, code).map("v" + _)
      },
      Option.when(target.y < position.y) {
        typeOnKeypad(keypad, position.up, code).map("^" + _)
      },
    ).flatten.flatten
  }
}

def parseInput(input: String): List[String] = {
  input.linesIterator.toList
}

val numericKeypad = Keypad(Vector(
  Vector('7', '8', '9'),
  Vector('4', '5', '6'),
  Vector('1', '2', '3'),
  Vector(' ', '0', 'A')
))
val directionalKeypad = Keypad(Vector(
  Vector(' ', '^', 'A'),
  Vector('<', 'v', '>'),
))

case class Keypad(matrix: Seq[Seq[Char]]) {
  val positionOf: Char => Position =
    (for {
      y <- matrix.indices
      x <- matrix.head.indices
    } yield matrix(y)(x) -> Position(x, y)).toMap
}

case class Position(x: Int, y: Int) {
  def left: Position = Position(x - 1, y)
  def right: Position = Position(x + 1, y)
  def up: Position = Position(x, y - 1)
  def down: Position = Position(x, y + 1)
}
